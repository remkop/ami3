package org.contentmine.ami.tools;

import java.io.File;

import java.io.IOException;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.contentmine.ami.tools.AMICleanTool;
import org.contentmine.ami.tools.AMIPDFTool;
import org.contentmine.cproject.files.CProject;
import org.junit.Assert;
import org.junit.Test;

/** test AMIProcessorPDF
 * 
 * @author pm286
 *
 */
public class AMIPDFTest extends AbstractAMITest {
	private static final Logger LOG = Logger.getLogger(AMIPDFTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	public final static File PDF2SVG2TEST = new File(PDF2SVG2, "text");
	
	@Test
	/** reads UCL corpus as PDFs and creates first pass SVG , images and scholarly html
	 * 
	 */
	public void testForestPlotsSmall() throws Exception {

//		log4j.appender.file=org.apache.log4j.DailyRollingFileAppender
//				log4j.appender.file.File=${user.home}/logs/app.log
//				log4j.appender.file.layout=org.apache.log4j.PatternLayout
//				log4j.appender.file.layout.ConversionPattern=%d [%t] %c %p %m%n
		String filename = "foo";
		boolean append = true;
		Layout layout = new PatternLayout();
		Appender appender = new FileAppender(layout, filename, append);
		String args = 
				"-p /Users/pm286/workspace/uclforest/forestplotssmall"
				+ " --rawfiletypes pdf"
				+ " --maxpages 20"
				+ " --pdfimages false"
				+ " --svgpages true"
				;
//		Assert.assertTrue(new File(args[1]).exists());
		AMIPDFTool amiProcessorPDF = new AMIPDFTool();
		amiProcessorPDF.runCommands(args);
		CProject cProject = amiProcessorPDF.getCProject();
		Assert.assertNotNull("CProject not null", cProject);
	}

	@Test
	/** reads UCL corpus as PDFs and creates first pass SVG , images and scholarly html
	 * 
	 */
	public void testForestPlotsSmallSVG() throws Exception {
		String projectDir = "/Users/pm286/workspace/uclforest/forestplotssmall";
		// delete the existing svg/ directories
		new AMICleanTool().runCommands("-p " + projectDir + " --dir svg/");
		// and then recreate them
		String[] args = {
				"-p", projectDir,
				"--rawfiletypes", "pdf",
				"--maxpages", "20",
				"--pdfimages", "false",
				"--svgpages", "true",
				};
		Assert.assertTrue(new File(args[1]).exists());
		AMIPDFTool amiProcessorPDF = new AMIPDFTool();
		amiProcessorPDF.runCommands(args);
		CProject cProject = amiProcessorPDF.getCProject();
		Assert.assertNotNull("CProject not null", cProject);
	}

	@Test
	/** reads UCL corpus as PDFs and creates first pass PDFImages
	 * 
	 */
	public void testForestPlotsSmallPDFImages() throws Exception {
		String projectDir = "/Users/pm286/workspace/uclforest/forestplotssmall";
		// delete the existing pdfimagesdirectories
//		new AMICleaner().runCommands("-p " + projectDir + " --dir pdfimages/");
		// and then recreate them
		String[] args = {
				"-p", projectDir,
				"--rawfiletypes", "pdf",
				"--maxpages", "20",
				"--pdfimages", "true",
				"--svgpages", "false",
				};
		AMIPDFTool amiProcessorPDF = new AMIPDFTool();
		amiProcessorPDF.runCommands(args);
	}

	@Test
	/** 
	 * convert whole project; 
	 */
	public void testDevProject() throws Exception {
		String args = ""
				+ "-p /Users/pm286/workspace/uclforest/dev/"
				+ " --forcemake"
				+ " --pdfimages true"
			;
		new AMIPDFTool().runCommands(args);
	}


	@Test
	/** 
	 * pearson did not emit vectors for page 18
	 * FIXED kludged 
	 */
	public void testVectorBug() throws Exception {
		String args = ""
				+ "-t /Users/pm286/workspace/uclforest/dev/pearson"
				+ " --forcemake"
			;
		new AMIPDFTool().runCommands(args);
	}

	@Test
	/** 
	 * campbell has implicit Move - not properly treated yet
	 */
	public void testVectorBugCampbell() throws Exception {
		String args = ""
				+ "-t /Users/pm286/workspace/uclforest/dev/campbell"
				+ " --forcemake"
			;
		new AMIPDFTool().runCommands(args);
	}


	@Test
	/** 
	 * this has very long paths and hangs with resource problems.
	 * The maxprimitives truncates these paths (thus losing data)
	 * but allows the job to complete. 
	 * 
	 */
	public void testLargeVector() throws Exception {
		String args = ""
				+ "-p "+CANADA+"/test"
				+ " --maxprimitives 10000"
				+ " --forcemake"
			;
		new AMIPDFTool().runCommands(args);
	}

	@Test
	/** new framework using AbstractPageParser.
	 * 
	 * @throws IOException
	 */
	public void testPDF2Framework() throws IOException {
		
		String args = "-p " + new File(PDF2SVG2, "test")
				+ " --maxprimitives 10000"
				+ " --debug AMI_TWO"
				+ " --forcemake"
				;
      
		new AMIPDFTool().runCommands(args);
//	    ParserDebug parserDebug = ParserDebug.ORIGINAL;
//		parserDebug = ParserDebug.AMI_MEDIUM;
//        int pageSerial = 0;
//        runPageDrawer(root, file, pageSerial, parserDebug, true);

	}

}
