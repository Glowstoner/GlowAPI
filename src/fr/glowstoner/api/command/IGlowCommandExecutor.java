package fr.glowstoner.api.command;

public interface IGlowCommandExecutor {
	
	public abstract void execute(String command, String [] args);
	
	public abstract String description();

}
