package aatr.le.gfx.texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

import java.nio.ByteBuffer;
import java.security.InvalidParameterException;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Vector4f;

import aatr.le.gfx.shader.OrthographicShaderProgram;
import aatr.le.gfx.shader.ShaderProgram;
import aatr.le.util.Util;

public class RenderableTexture extends Texture {
	
	public static final Vector4f DEFAULT_BACKGROUND_COLOR = new Vector4f(0, 0, 0, 1);
	public static final int DEFAULT_INTERNAL_FORMAT = GL_RGBA8;
	public static final int DEFAULT_ATTACHMENT = GL_COLOR_ATTACHMENT0;
	
	public static final int DEFAULT_WIDTH = 128;
	public static final int DEFAULT_HEIGHT = 128;
	
	private boolean hasDepthBuffer;
	
	private Vector4f bgc = DEFAULT_BACKGROUND_COLOR;
	
	private int fbo = -2;
	private int rbo = -2;
	
	public RenderableTexture() {}
	
	public RenderableTexture(int width, int height) {
		this(width, height, DEFAULT_INTERNAL_FORMAT);
	}
	
	public RenderableTexture(int width, int height, int internalFormat) {
		this(width, height, internalFormat, new int[]{DEFAULT_ATTACHMENT});
	}
	
	public RenderableTexture(int width, int height, int internalFormat, int[] attachments) {
		this(width, height, internalFormat, attachments, false);
	}
	
	public RenderableTexture(int width, int height, int internalFormat, int[] attachments, boolean useDepthBuffer) {
		initialize(width, height, internalFormat, attachments, useDepthBuffer);
	}
	
	public RenderableTexture initialize(int width, int height, int internalFormat, int[] attachments, boolean useDepthBuffer) {
		if(!GLContext.getCapabilities().GL_EXT_framebuffer_object) {
			System.err.println("FrameBuffers not supported on your graphics card!");
		}
		
		this.width = width;
		this.height = height;
		hasDepthBuffer = useDepthBuffer;
		
		id = glGenTextures();
		bind();
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
		
		int type;
		
		switch(internalFormat) {
		default: type = GL_UNSIGNED_BYTE; break;
		case GL_DEPTH_COMPONENT:
		case GL_DEPTH_COMPONENT16:
		case GL_DEPTH_COMPONENT24:
		case GL_DEPTH_COMPONENT32: type = GL_FLOAT; break;
		}
		
		glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0,
				OrthographicShaderProgram.INSTANCE.getOutputFormat(), type, (ByteBuffer)null);
		
		unbind();
		
		if(useDepthBuffer) {
			rbo = glGenRenderbuffers();
			glBindRenderbuffer(GL_RENDERBUFFER, rbo);
			glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, width, height);
			glBindRenderbuffer(GL_RENDERBUFFER, 0);
		}
		
		fbo = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		
		if(useDepthBuffer)
			glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rbo);
		glFramebufferTexture2D(GL_FRAMEBUFFER, attachments[0], GL_TEXTURE_2D, id, 0);
		
		int[] drawBuffers = new int[attachments.length];
		
		for(int i = 0; i < attachments.length; i++)
			if(attachments[i] == GL_DEPTH_ATTACHMENT)
				drawBuffers[i] = GL_NONE;
			else
				drawBuffers[i] = attachments[i];
		
		glDrawBuffers(Util.toIntBuffer(drawBuffers));
		
		checkFramebufferErrors();
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		
		return this;
	}
	
	public RenderableTexture checkFramebufferErrors() {
		
		String log = null;
		
		switch(glCheckFramebufferStatus(GL_FRAMEBUFFER)) {
		case 0: log = "Undefined error!"; break;
		case GL_FRAMEBUFFER_UNDEFINED: log = "Target is default framebuffer and does not exist!"; break;
		case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT: log = "At least one attachment point is framebuffer incomplete!"; break;
		case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT: log = "Framebuffer has no attachments!"; break;
		case GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER: log = "GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE is GL_NONE for at least one color attachment point!"; break;
		case GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER: log = "GL_READ_BUFFEr is not GL_NONE and GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE is GL_NONE for the color attachment point named by GL_READ_BUFFER!"; break;
		case GL_FRAMEBUFFER_UNSUPPORTED: log = "Combination of internal formats violate an implementation-dependent set of restrictions!"; break;
		case GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE: log = "The value of GL_RENDERBUFFER_SAMPLES is not the same for all attached textures; or, the value of GL_TEXTURE_SAMPLES is not the same for all attached textures; or, the attachments are a mix of renderbuffers and textures; or, the value of GL_RENDERBUFFER_SAMPLES does not match the value of GL_TEXTURE_SAMPLES; or, the value of GL_TEXTURE_FIXED_SAMPLE_LOCATIONS is not the same for all attached textures; or, the value of GL_TEXTURE_FIXED_SAMPLE_LOCATION is not GL_TRUE for all attached textures!"; break;
		case GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS: log = "At least one framebuffer attachment is layered, and at least one populated attachment is not layered; or, all populated color attachments are not from textures of the same target!"; break;
		}
		
		if(log != null)
			System.err.println(log + " Framebuffer id: " + fbo);
		
		return this;
	}
	
	public RenderableTexture releaseRenderTarget() {
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		return this;
	}
	
	public RenderableTexture bindAsRenderTarget(boolean clear) {
		glViewport(0, 0, width, height);
		unbind();
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, fbo);
		glClearColor(bgc.x, bgc.y, bgc.z, bgc.w);
		if(clear)
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		return this;
	}
	
	public RenderableTexture setBackground(Vector4f bgc) {
		this.bgc = bgc;
		return this;
	}
	
	public RenderableTexture setBackground(float x, float y, float z, float w) {
		return setBackground(new Vector4f(x, y, z, w));
	}
	
	public Vector4f getBackground() {
		return bgc;
	}
	
	public boolean hasDepthBuffer() {
		return hasDepthBuffer;
	}
}