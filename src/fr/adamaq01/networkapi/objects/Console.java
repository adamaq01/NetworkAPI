package fr.adamaq01.networkapi.objects;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.impl.DumbTerminal;

public abstract class Console implements Runnable {

	private LineReader reader;
	private Thread thread;

	private String prompt;

	public Console(String name) throws IOException {
		thread = new Thread(this);
		thread.setName(name);
		prompt = "> ";
		boolean buggy = false;
		Terminal terminal = null;
		try {
			terminal = TerminalBuilder.builder().name(name).encoding(Charset.defaultCharset().name()).build();
		} catch (Exception e) {
			terminal = new DumbTerminal(name, null, System.in, System.out, Charset.defaultCharset().name());
			buggy = true;
		}
		reader = LineReaderBuilder.builder().terminal(terminal).appName(name).build();
		if (buggy)
			error("Le terminal utilisé pour lancer ce serveur n'est pas compatible, il est donc buggé !");
		thread.start();
	}

	@Override
	public void run() {
		while (true) {
			reader.getTerminal().writer().flush();
			String raw = reader.readLine(prompt);
			String command = raw.split(" ")[0];
			String[] args = raw.replaceFirst(command + " ", "").split(" ");
			onCommand(command, args);
			info("Commande utilisée: " + command);
		}
	}

	public void info(String message) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		reader.getTerminal().writer().println("[" + format.format(new Date()) + " INFO]: " + message);
		reader.getTerminal().writer().flush();
	}

	public void error(String message) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		reader.getTerminal().writer().println("[" + format.format(new Date()) + " ERROR]: " + message);
		reader.getTerminal().writer().flush();
	}

	public abstract void onCommand(String command, String[] args);

}
