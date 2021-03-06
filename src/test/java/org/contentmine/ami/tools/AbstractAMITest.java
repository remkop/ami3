package org.contentmine.ami.tools;

import java.io.File;
import java.io.InputStream;

import org.contentmine.cproject.files.CProject;
import org.contentmine.cproject.util.CMineUtil;
import org.junit.Assert;
import org.junit.Test;

public class AbstractAMITest {
	
	public static File _HOME = new File("/Users/pm286");
	public static File CMDEV = new File(_HOME, "workspace/cmdev");
	public static final String CONTENTMINE = "ami3/src/test/resources/org/contentmine";
	public static File CMINE = new File(CMDEV, CONTENTMINE);
	public static File SRC_TEST_AMI = new File(CMINE, "ami");
	public static File SRC_TEST_GRAPHICS = new File(CMINE, "graphics");
	public static File SRC_TEST_SVG = new File(SRC_TEST_GRAPHICS, "svg");
	public static File SRC_TEST_PLOT = new File(SRC_TEST_SVG, "plot");
	public static File SRC_TEST_TOOLS = new File(SRC_TEST_AMI, "tools");
	public static File SRC_TEST_DOWNLOAD = new File(SRC_TEST_TOOLS, "download");
	public static File PDF2SVG2 = new File(SRC_TEST_AMI, "pdf2svg2");
	public static File OIL5 = new File(SRC_TEST_AMI, "oil5/");
	
	
	public static File WORKSPACE = new File(_HOME, "workspace/");
	public static File PROJECTS = WORKSPACE.exists() ? new File(WORKSPACE, "projects/") : new File(_HOME, "projects/");
	public static File CANADA = new File(PROJECTS, "canada/");
	public static File CEV = new File(PROJECTS, "CEVOpen/").exists() ? new File(PROJECTS, "CEVOpen/") : new File(PROJECTS, "CEV/");
	public static File CEV_SEARCH = new File(CEV, "searches/");
	public static File CLIMATE = new File(PROJECTS, "climate/");
	public static File CLIM_SEARCH = new File(CLIMATE, "searches/");
	public static File CLIMATE200 = new File(CLIMATE, "climate200");
	public static File CLIMATE200SECTIONS = new File(CLIMATE, "climate200sections");
	public static File CMIP200 = new File(CLIM_SEARCH, "cmip200/");
	
	public static File OIL186 = new File(CEV_SEARCH, "oil186/");
	public static File OIL1000 = new File(CEV_SEARCH, "oil1000/");
	public static CProject OIL186_PROJ = new CProject(OIL186);

	
	@Test
	public void testPython() throws Exception {
//		ProcessBuilder builder = new ProcessBuilder("java", "-version");
		String[] args = {"java", "-version"};
		CMineUtil.runProcess(args, (InputStream) null);
		
		args = new String[]{"python", "--help"};
		CMineUtil.runProcess(args, (InputStream) null);
		
//		args = new String[]{"node", "--help"};
//		CMineUtil.runProcess(args, (InputStream) null);
		
//		args = new String[]{"bashx", "-xx"};
		args = new String[]{"bash", "-xx"};
		CMineUtil.runProcess(args, (InputStream) null);
		
	}
	
	@Test
	public void testRelativeFile() {
		CProject cProject = new CProject(OIL5);
		AMISearchTool tool = new AMISearchTool(cProject);
		File file = tool.getFileRelativeToProject("../zika10");
		Assert.assertNotNull("file", file);
		file = tool.getFileRelativeToProject("../oil186");
		Assert.assertNull("file", file);
		file = tool.getFileRelativeToProject("/Users/");
		Assert.assertNotNull("file", file);
	}
}
