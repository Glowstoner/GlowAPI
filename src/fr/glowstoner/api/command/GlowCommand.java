package fr.glowstoner.api.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlowCommand {
	
	private static Map<String, Object> commands = new HashMap<>();

	public GlowCommand(String cmd, Object instance) {
		if(!commands.containsKey(cmd)) {
			commands.put(cmd.toUpperCase(), instance);
		}
	}
	
	public GlowCommand() {
		
	}
	
	public void registerCommand(String cmd, IGlowCommandExecutor instance) {
		if(!commands.containsKey(cmd)) {
			commands.put(cmd.toUpperCase(), instance);
		}
	}
	
	public boolean hasCommand(String cmd) {
		if(commands.containsKey(cmd.toUpperCase())) {
			return true;
		}else {
			return false;
		}
	}
	
	public List<String> getCommandsList(){
		List<String> list = new ArrayList<String>();
		
		for(String cmds : commands.keySet()) {
			list.add(cmds);
		}
		
		return list;
	}
	
	public void executeCommand(String cmd, String[] args) throws InstantiationException, IllegalAccessException, 
		NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		
		if(commands.containsKey(cmd.toUpperCase())) {
			Object instance = commands.get(cmd.toUpperCase());
				
			Method m = instance.getClass().getDeclaredMethod("execute", String.class, String[].class);
			m.invoke(instance, cmd, (Object) args);
		}
	}
	
	public String getDescription(String cmd) throws NoSuchMethodException, SecurityException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Object o = commands.get(cmd.toUpperCase());
		
		Method m = o.getClass().getDeclaredMethod("description");
		return (String) m.invoke(o);
	}
}
