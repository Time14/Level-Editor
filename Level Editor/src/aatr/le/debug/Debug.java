package aatr.le.debug;

public class Debug {
	
	public static void log(Object out) { 
        StackTraceElement st = Thread.currentThread().getStackTrace()[2];
        System.out.println("[" + st.getFileName().split("[.]")[0] + ":" + st.getLineNumber() + "] " + out.toString());
	}
	
	public static void log(Object... out) {
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < out.length; i++) {
			sb.append(out[i].toString());
			if(i < out.length - 1)
				sb.append("\t");
		}
		
        StackTraceElement st = Thread.currentThread().getStackTrace()[2];
        System.out.println("[" + st.getFileName().split("[.]")[0] + ":" + st.getLineNumber() + "] " + sb.toString());
	}
	
	public static void error(Object out) { 
        StackTraceElement st = Thread.currentThread().getStackTrace()[2];
        System.err.println("[" + st.getFileName().split("[.]")[0] + ":" + st.getLineNumber() + "] " + out.toString());
	}
}
