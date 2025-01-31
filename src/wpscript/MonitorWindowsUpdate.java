package wpscript;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MonitorWindowsUpdate {

	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private static final int INTERVAL_MINUTES = 15;
	private static LocalDateTime nextExecutionTime;

	public static void main(String[] args) {
		System.out.println("Monitoramento iniciado: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
		monitorService();
		scheduleNextExecution();
	}

	private static void scheduleNextExecution() {
		nextExecutionTime = LocalDateTime.now().plusMinutes(INTERVAL_MINUTES);
		System.out.println("Próxima verificação: " + formatTime(nextExecutionTime));
		scheduler.scheduleAtFixedRate(() -> {
			monitorService();
			nextExecutionTime = nextExecutionTime.plusMinutes(INTERVAL_MINUTES);
			System.out.println("Próxima verificação: " + formatTime(nextExecutionTime));
		}, INTERVAL_MINUTES, INTERVAL_MINUTES, TimeUnit.MINUTES);
	}

	private static String formatTime(LocalDateTime time) {
		return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
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
		executeCommand("sc config wuauserv start= disabled");
		executeCommand("net stop wuauserv");
		System.out.println("Serviço desabilitado e parado.");
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
