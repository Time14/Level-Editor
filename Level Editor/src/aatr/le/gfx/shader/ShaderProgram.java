package aatr.le.gfx.shader;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.HashMap;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import aatr.le.gfx.shader.StructDefiner.GLSLType;
import aatr.le.util.Loader;
import aatr.le.util.Util;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;

public abstract class ShaderProgram {
	
	protected static int currentProgram;
	
	private int id = -2;
	
	protected HashMap<String, Integer> ul;
	
	public ShaderProgram(String vshp, String fshp) {
		
		int vsh = createShader(vshp, GL_VERTEX_SHADER);
		int fsh = createShader(fshp, GL_FRAGMENT_SHADER);
		
		id = glCreateProgram();
		
		glAttachShader(id, vsh);
		glAttachShader(id, fsh);
		
		glLinkProgram(id);
		
		if(glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
			System.err.println("Error linking shader program \"" + id + "\"");
			System.err.println("-------------------------------- START --------------------------------");
			System.err.println(glGetProgramInfoLog(id, glGetProgrami(id, GL_INFO_LOG_LENGTH)));
			System.err.println("--------------------------------  END  --------------------------------");
		}
		
		glDetachShader(id, vsh);
		glDetachShader(id, fsh);
		glDeleteShader(vsh);
		glDeleteShader(fsh);
		
		ul = new HashMap<>();
		registerUniformLocations();
	}
	
	private int createShader(String path, int type) {
		
		int shader = glCreateShader(type);
		glShaderSource(shader, Loader.loadSource(path));
		glCompileShader(shader);
		
		if(glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Error in " + (type == GL_VERTEX_SHADER ? "vertex" : "fragment") + " shader at \"" + path + "\"");
			System.err.println("-------------------------------- START --------------------------------");
			System.err.println(glGetShaderInfoLog(shader, glGetShaderi(shader, GL_INFO_LOG_LENGTH)));
			System.err.println("--------------------------------  END  --------------------------------");
		}
	
		return shader;
	}
	
	public void bind() {
		if(id == currentProgram)
			return;
		glUseProgram(id);
		currentProgram = id;
	}
	
	public int getID() {
		return id;
	}
	
	protected abstract void registerUniformLocations();
	public abstract void initAttributes();
	public abstract int getOutputFormat();
	
	protected void registerUniformLocation(String uniform) {
		ul.put(uniform, getUniformLocation(uniform));
	}
	
	protected void registerUniformArrayLocations(String uniform, int length) {
		for(int i = 0; i < length; i++)
			registerUniformLocation(uniform + "[" + i + "]");
	}
	
	protected void registerUniformStructLocation(String uniform, String[] comps) {
		for(String comp : comps)
			registerUniformLocation(uniform + "." + comp);
	}
	
	protected void registerUniformStructArrayLocations(String uniform, String[] comps, int length) {
		for(int i = 0; i < length; i++)
			registerUniformStructLocation(uniform + "[" + i + "]", comps);
	}
	
	private int getUniformLocation(String uniform) {
		return glGetUniformLocation(id, uniform);
	}
	
	public ShaderProgram sendMatrix(String target, Matrix4f matrix) {
		bind();
		glUniformMatrix4(ul.get(target), false, Util.toFloatBuffer(matrix));
		return this;
	}
	
	public ShaderProgram sendMatrixArray(String target, Matrix4f[] matrices) {
		for(int i = 0; i < matrices.length; i++)
			sendMatrix(target + "[" + i + "]", matrices[i]);
		return this;
	}
	
	public ShaderProgram sendInt(String target, int i) {
		bind();
		glUniform1i(ul.get(target), i);
		return this;
	}
	
	public ShaderProgram sendIntArray(String target, int[] data) {
		for(int i = 0; i < data.length; i++)
			sendInt(target + "[" + i + "]", data[i]);
		return this;
	}
	
	public ShaderProgram sendFloat(String target, float f) {
		bind();
		glUniform1f(ul.get(target), f);
		return this;
	}
	
	public ShaderProgram sendFloatArray(String target, float[] data) {
		for(int i = 0; i < data.length; i++)
			sendFloat(target + "[" + i + "]", data[i]);
		return this;
	}
	
	public ShaderProgram sendVec2(String target, Vector2f vec2) {
		bind();
		glUniform2f(ul.get(target), vec2.x, vec2.y);
		return this;
	}
	
	public ShaderProgram sendVec2Array(String target, Vector2f[] data) {
		for(int i = 0; i < data.length; i++)
			sendVec2(target + "[" + i + "]", data[i]);
		return this;
	}
	
	public ShaderProgram sendVec3(String target, Vector3f vec3) {
		bind();
		glUniform3f(ul.get(target), vec3.x, vec3.y, vec3.z);
		return this;
	}
	
	public ShaderProgram sendVec3Array(String target, Vector3f[] data) {
		for(int i = 0; i < data.length; i++)
			sendVec3(target + "[" + i + "]", data[i]);
		return this;
	}
	
	public ShaderProgram sendVec4(String target, Vector4f vec4) {
		bind();
		glUniform4f(ul.get(target), vec4.x, vec4.y, vec4.z, vec4.w);
		return this;
	}
	
	public ShaderProgram sendVec4Array(String target, Vector4f[] data) {
		for(int i = 0; i < data.length; i++)
			sendVec4(target + "[" + i + "]", data[i]);
		return this;
	}
	
	public ShaderProgram sendStruct(String target, StructDefiner struct) {
		bind();
		
		float[] data = struct.getData();
		
		int offset = 0;
		for(int i = 0; i < struct.getStructure().length; i++) {
			GLSLType type = struct.getStructure()[i];
			String location = target + "." + struct.getComponentNames()[i];
			switch(type) {
			case FLOAT:
			case INT:
			case DOUBLE:
				glUniform1f(ul.get(location), data[offset]);
				break;
			case VEC2:
				glUniform2f(ul.get(location), data[offset], data[offset + 1]);
				break;
			case VEC3:
				glUniform3f(ul.get(location), data[offset], data[offset + 1], data[offset + 2]);
				break;
			case VEC4:
				glUniform4f(ul.get(location), data[offset], data[offset + 1], data[offset + 2], data[offset + 3]);
				break;
			case MAT4:
				glUniformMatrix4(ul.get(location), false, Util.toFloatBuffer(Arrays.copyOfRange(data, offset, offset + GLSLType.MAT4.LENGTH - 1)));
				break;
			}
			offset += type.LENGTH;
		}
		
		return this;
	}
	
	public ShaderProgram sendStructArray(String target, StructDefiner[] structs) {
		for(int i = 0; i < structs.length; i++)
			sendStruct(target + "[" + i + "]", structs[i]);
		return this;
	}
	
	public ShaderProgram sendBoolean(String target, boolean bool) {
		bind();
		glUniform1i(ul.get(target), bool ? 1 : 0);
		return this;
	}
	
	public ShaderProgram sendBooleanArray(String target, boolean[] data) {
		for(int i = 0; i < data.length; i++)
			sendBoolean(target + "[" + i + "]", data[i]);
		return this;
	}
	
	public void destroy() {
		glDeleteProgram(id);
		System.out.println("Destroyed program with ID: " + id);
	}
	
	public static final int currentlyBoundID() {
		return currentProgram;
	}
}