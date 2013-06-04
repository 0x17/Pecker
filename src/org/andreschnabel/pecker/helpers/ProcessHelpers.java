package org.andreschnabel.pecker.helpers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Hilfsfunktionen für den Umgang mit Prozessen.
 */
public class ProcessHelpers {
	/**
	 * Führe Prozess in gegebenem Arbeitsverzeichnis aus.<br />
	 * Kommando und Parameter werden in String-Array "command" übergeben.<br />
	 * Versucht unter Windows bei Scheitern noch mit Präfix "cmd /c".
	 * @param workDir Arbeitsverzeichnis zur Ausführung des Befehls.
	 * @param command Array aus [Befehl,Parameter1,...,ParameterN].
	 * @return Ausgabe von Befehl auf stderr und stdout.
	 * @throws Exception
	 */
	public static String monitorProcess(File workDir, String... command) throws Exception {
		try {
			ProcessBuilder pb = new ProcessBuilder(command);
			pb.directory(workDir);

			pb.redirectErrorStream(true);
			Process p = pb.start();
			StreamTapper st = new StreamTapper(p.getInputStream());
			st.run();

			p.waitFor();

			return st.getOutput();
		} catch(Exception e) {
			if(Helpers.runningOnUnix() || command[0].equals("cmd")) {
				throw e;
			} else {
				try {
					return tryWithCmdPrefix(workDir, command);
				} catch(Exception e2) {
					throw e2;
				}
			}
		}
	}

	/**
	 * Windows durchsucht sonst PATH nicht.
	 * @param workDir Arbeitsverzeichnis.
	 * @param command [Befehl,Param1,...,ParamN].
	 * @return Ausgabe in stdout vereinigt mit stderr.
	 * @throws Exception
	 */
	private static String tryWithCmdPrefix(File workDir, String[] command) throws Exception {
		String[] cmds = new String[command.length+2];
		cmds[0] = "cmd";
		cmds[1] = "/c";
		for(int i=0; i<command.length; i++) {
			cmds[2+i] = command[i];
		}
		return monitorProcess(workDir, cmds);
	}

	/**
	 * Führe Befehl mit Parametern in Zeichenkette aus.
	 * Gebe Ausgabe auf stdout aus.
	 * @param cmd Befehl (evtl. mit Parametern) "befehl param1 param2 ... paramN"
	 */
	public static void system(String cmd) {
		Helpers.log("Running: " + cmd);
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			StreamTapper st = new StreamTapper(p.getInputStream());
			st.run();

			p.waitFor();

			Helpers.logNoNewline(st.getOutput());
		} catch(Exception e) {

			if(Helpers.runningOnUnix() || cmd.split(" ")[0].equals("cmd")) {
				Helpers.log("Error executing: " + cmd);
				e.printStackTrace();
			} else {
				try {
					trySystemWithCmdPrefix(cmd);
				} catch(Exception e2) {
					Helpers.log("Error executing: " + cmd);
					e.printStackTrace();
				}
			}
		}
	}

	private static void trySystemWithCmdPrefix(String cmd) {
		system("cmd /c " + cmd);
	}

	/**
	 * Führe Befehl in Arbeitsverzeichnis. Befehl und Parameter in Array.
	 * Gebe Ausgabe von stdout und stderr direkt weiter an stdout.
	 * @param workDir Arbeitsverzeichnis
	 * @param command [Befehl,Param1,...,ParamN]
	 * @throws Exception
	 */
	public static void system(File workDir, String... command) throws Exception {
		try {
			Helpers.logNoNewline("Running: \"");
			for(String cmd : command) {
				Helpers.logNoNewline(cmd + " ");
			}
			Helpers.logNoNewline("\" in " + workDir.getAbsolutePath() + "\n");

			ProcessBuilder pb = new ProcessBuilder(command);
			pb.directory(workDir);

			pb.redirectErrorStream(true);
			Process p = pb.start();

			// Horrible workaround since 'git clone' seems to hang when 'Cloning into..." from time to time w/out
			// valid reason.
			boolean gitCloneHangingWorkaround = isGitClone(command);
			StreamTapper st = new StreamTapper(p.getInputStream(), true, gitCloneHangingWorkaround, gitCloneHangingWorkaround ? p : null);

			st.start();

			p.waitFor();

			if(gitCloneHangingWorkaround && st.getTimeout()) {
				Helpers.log("Git clone command was killed due to hanging 1 minute in 'Cloning into' state.");
			}

		} catch(Exception e) {
			if(Helpers.runningOnUnix() || command[0].equals("cmd")) {
				throw e;
			} else {
				try {
					trySystemWithCmdPrefix(workDir, command);
				} catch(Exception e2) {
					throw e2;
				}
			}
		}
	}

	private static boolean isGitClone(String[] cmds) {
		boolean foundClone = false;
		boolean foundGit = false;
		for(String cmd : cmds) {
			if(cmd.equals("clone"))
				foundClone = true;
			else if(cmd.endsWith("git") || cmd.endsWith("git.exe"))
				foundGit = true;
		}
		return foundGit && foundClone;
	}

	private static void trySystemWithCmdPrefix(File workDir, String[] command) throws Exception {
		String[] cmds = new String[command.length+2];
		cmds[0] = "cmd";
		cmds[1] = "/c";
		for(int i=0; i<command.length; i++) {
			cmds[2+i] = command[i];
		}
		system(workDir, cmds);
	}

	private static class CloneKillThread extends Thread {
		// Kill cloning after one minute of blocking "Cloning into".
		private static final long CLONE_MAX_MS = 60 * 1000;

		private final long cloneStartTime;
		private final Process cloneProcess;
		private volatile boolean done;
		private volatile boolean timeout;

		public CloneKillThread(Process cloneProcess) {
			this.cloneProcess = cloneProcess;
			this.cloneStartTime = System.currentTimeMillis();
		}

		@Override
		public void run() {
			super.run();
			while(!done) {
				if(System.currentTimeMillis() - cloneStartTime > CLONE_MAX_MS) {
					cloneProcess.destroy();
					timeout = true;
				} else {
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		public synchronized boolean done() {
			done = true;
			return timeout;
		}
	}

	private static class StreamTapper extends Thread {
		private InputStream is;
		private boolean forwardToStdOut;
		private String output;
		private volatile boolean done;
		private volatile boolean timeout;

		private final boolean gitCloneHangingWorkaround;
		private CloneKillThread cloneKillThread;
		private final Process cloneProcess;

		private StreamTapper(InputStream is) {
			this(is, false);
		}

		private StreamTapper(InputStream is, boolean forwardToStdOut) {
			this(is, forwardToStdOut, false, null);
		}

		public StreamTapper(InputStream is, boolean forwardToStdOut, boolean gitCloneHangingWorkaround, Process cloneProcess) {
			this.is = is;
			this.forwardToStdOut = forwardToStdOut;
			this.gitCloneHangingWorkaround = gitCloneHangingWorkaround;
			this.cloneProcess = cloneProcess;
		}

		@Override
		public void run() {
			super.run();
			InputStreamReader isr = new InputStreamReader(is);
			StringBuilder outputBuilder = new StringBuilder();
			int c;
			try {
				while((c = isr.read()) != -1 && !done) {
					outputBuilder.append((char) c);
					if(forwardToStdOut) {
						Helpers.log(c);
					}

					// Horrible workaround. We need to kill git-clone if it stays in "Cloning into" output too long.
					// POST git-upload-pack is sign things went fine.
					if(gitCloneHangingWorkaround) {
						String o = outputBuilder.toString();
						String lline = StringHelpers.lastLine(o);
						if(lline.startsWith("Cloning into") && cloneKillThread == null) {
							cloneKillThread = new CloneKillThread(cloneProcess);
							cloneKillThread.start();
						} else if(lline.startsWith("POST git-upload-pack") && cloneKillThread != null) {
							if(cloneKillThread.done()) {
								timeout = true;
							}
							cloneKillThread = null;
						}
					}
				}

				if(cloneKillThread != null) {
					if(cloneKillThread.done()) {
						timeout = true;
					}
					cloneKillThread = null;
				}
			} catch(IOException e) {
				e.printStackTrace();
			}

			output = outputBuilder.toString();
		}

		public synchronized boolean getTimeout() {
			return timeout;
		}

		public synchronized String getOutput() {
			done = true;
			return output;
		}
	}

}
