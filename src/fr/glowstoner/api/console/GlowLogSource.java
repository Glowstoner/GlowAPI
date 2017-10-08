package fr.glowstoner.api.console;

import fr.glowstoner.api.files.GlowModule;

public class GlowLogSource {
	
	private String sourceName;
	private SourceType type;
	private GlowModule module;
	
	public GlowLogSource(SourceType type, GlowModule module) {
		if(type.equals(SourceType.BASE)) {
			this.type = SourceType.BASE;
		}else if(type.equals(SourceType.MODULE)) {
			this.type = SourceType.MODULE;
			this.setModule(module);
		}
	}
	
	public GlowLogSource(GlowModule module) {
		this.setModule(module);
		this.type = SourceType.MODULE;
	}
	
	public String getRender() {
		if(type.equals(SourceType.BASE)) {
			return "[CONSOLE] ";
		}else if(type.equals(SourceType.MODULE)) {
			return "["+module.getModuleName()+"] ";
		}else {
			return null;
		}
	}
	
	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public SourceType getType() {
		return type;
	}

	public void setType(SourceType type) {
		this.type = type;
	}

	public GlowModule getModule() {
		return module;
	}

	public void setModule(GlowModule module) {
		this.module = module;
	}
}
