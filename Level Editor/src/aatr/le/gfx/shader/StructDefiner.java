package aatr.le.gfx.shader;

public interface StructDefiner {
	
	public GLSLType[] getStructure();
	
	public String[] getComponentNames();
	
	public float[] getData();
	
	public enum GLSLType {
		FLOAT(1), INT(1), DOUBLE(1), VEC2(2), VEC3(3), VEC4(4), MAT4(16);
		
		public final int LENGTH;
		
		private GLSLType(int length) {
			LENGTH = length;
		}
	}
}