package wpscript;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MonitorWindowsUpdate {

	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public static void main(String[] args) {
		scheduler.scheduleAtFixedRate(MonitorWindowsUpdate::monitorService, 0, 5, TimeUnit.MINUTES);
	}

	private static void monitorService() {
		if (isServiceRunning()) {
			stopAndDisableService();
		} else {
			System.out.println("O serviço Windows Update não está em execução.");
		}
	}

	private static boolean isServiceRunning() {
		String command = "powershell.exe -Command \"Get-Service wuauserv | Select-Object -ExpandProperty Status\"";
		String status = executeCommand(command);
		return "Running".equalsIgnoreCase(status.trim());
	}

	private static void stopAndDisableService() {
		System.out.println("Serviço Windows Update ativo. Parando e desabilitando...");
		executeCommand("net stop wuauserv");
		executeCommand("sc config wuauserv start= disabled");
		System.out.println("Serviço parado e desabilitado.");
	}

	private static String executeCommand(String command) {
		try {
			Process process = new ProcessBuilder("cmd.exe", "/c", command).start();
			process.waitFor();
			return readProcessOutput(process);
		} catch (IOException | InterruptedException e) {
			handleError(e);
		}
		return "";
	}

	private static String readProcessOutput(Process process) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			StringBuilder output = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line).append("\n");
			}
			return output.toString();
		}
	}

	private static void handleError(Exception e) {
		e.printStackTrace();
	}
}
