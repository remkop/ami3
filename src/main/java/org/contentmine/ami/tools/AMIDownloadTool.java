package org.contentmine.ami.tools;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.contentmine.ami.tools.AMIDictionaryTool.RawFileFormat;
import org.contentmine.ami.tools.download.AbstractDownloader;
import org.contentmine.ami.tools.download.AbstractLandingPage;
import org.contentmine.ami.tools.download.AbstractMetadataEntry;
import org.contentmine.ami.tools.download.FullFileManager;
import org.contentmine.ami.tools.download.FulltextManager;
import org.contentmine.ami.tools.download.LandingPageManager;
import org.contentmine.ami.tools.download.QueryManager;
import org.contentmine.ami.tools.download.biorxiv.BiorxivDownloader;
import org.contentmine.ami.tools.download.biorxiv.BiorxivLandingPage;
import org.contentmine.ami.tools.download.biorxiv.BiorxivMetadataEntry;
import org.contentmine.ami.tools.download.hal.HALDownloader;
import org.contentmine.ami.tools.download.hal.HALLandingPage;
import org.contentmine.ami.tools.download.hal.HALMetadataEntry;
import org.contentmine.ami.tools.download.osf.OSFDownloader;
import org.contentmine.ami.tools.download.osf.OSFLandingPage;
import org.contentmine.ami.tools.download.osf.OSFMetadataEntry;
import org.contentmine.ami.tools.download.sd.SDDownloader;
import org.contentmine.ami.tools.download.sd.SDLandingPage;
import org.contentmine.ami.tools.download.sd.SDMetadataEntry;
import org.contentmine.cproject.files.CProject;
import org.contentmine.cproject.files.CTree;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
/**
 * 
 * @author pm286
 *
 */

@Command(
name = "ami-download", 
aliases = "download",
version = "ami-download 0.1",
description = "downloads content from remote site. Maybe a wrapper for getpapers, curl, etc."
) 

/** see org.contentmine.ami.tools.download.AbstractDownloader for mechanism and options
 * 
 * @author pm286
 *
 */

public class AMIDownloadTool extends AbstractAMITool {

	private static final Logger LOG = Logger.getLogger(AMIDownloadTool.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	public enum SearchSite {
		biorxiv(
				new BiorxivDownloader(),
				new BiorxivMetadataEntry(),
				new BiorxivLandingPage()
				),
		hal(
				new HALDownloader(),
				new HALMetadataEntry(),
				new HALLandingPage()
				),
		osf(
				new OSFDownloader(),
				new OSFMetadataEntry(),
				new OSFLandingPage()
				),
		sd(
				new SDDownloader(), 
				new SDMetadataEntry(), 
				new SDLandingPage()),
		;
		private String site;
		private AbstractDownloader downloader;
		private AbstractMetadataEntry metadata;
		private AbstractLandingPage landingPage;
		
		private SearchSite( 
				AbstractDownloader downloader, 
				AbstractMetadataEntry metadata, 
				AbstractLandingPage landingPage) {
			this.downloader = downloader;
			this.metadata = metadata;
			this.landingPage = landingPage;
			this.site = downloader.getSearchUrl();
		}
		public String getSite() {
			return site;
		}

		public AbstractDownloader createDownloader(AMIDownloadTool downloadTool) {
			AbstractDownloader newDownloader = null;
			try {
				newDownloader = (AbstractDownloader) Class.forName(downloader.getClass().getName()).newInstance();
				newDownloader.setDownloadTool(downloadTool);
			} catch (Exception e) {
				throw new RuntimeException("BUG: ", e);
			}
			return newDownloader;
		}

		public AbstractMetadataEntry createMetadata() {
			AbstractMetadataEntry newMetadata = null;
			try {
				newMetadata = (AbstractMetadataEntry) Class.forName(metadata.getClass().getName()).newInstance();
			} catch (Exception e) {
				throw new RuntimeException("BUG: ", e);
			}
			return newMetadata;
		}
		
		public AbstractLandingPage createNewLandingPageObject() {
			AbstractLandingPage newLandingPage = null;
			try {
				newLandingPage = (AbstractLandingPage) Class.forName(landingPage.getClass().getName()).newInstance();
			} catch (Exception e) {
				throw new RuntimeException("BUG: ", e);
			}
			return newLandingPage;
		}
	}
	
	public enum Cleanness {
		raw,
		clean
	}

    @Option(names = {"--landingpage"},
    		arity = "1",
            description = "")
    private String landingPage;

    @Option(names = {"--limit"},
    		arity = "1",
            description = "max hits to download (default 200), set to (pages * pagesize) if both set ")
    private Integer limit = 200;

    @Option(names = {"--metadata"},
    		arity = "1",
            description = "directory for metadata pages")
    private String metadata = "metadata";

    @Option(names = {"--pages"},
    		arity = "1..2",
            description = "start and optional end hitpage (1-based, inclusive). If only one value download "
            		+ "single hitpage, default 1 ")
    private List<Integer> pageList = new ArrayList<>();

    @Option(names = {"--pagesize"},
    		arity = "1",
            description = "size of hit page, no default (often set by service)")
    private Integer pagesize = null;

    @Option(names = {"--query"},
    		arity = "1..*",
            description = "query to issue (may need escaping)")
    private List<String> queryList = null;

    @Option(names = {"--resultset"},
    		arity = "1..*",
            description = "resultSets to download (filenames, experimental). If omitted, "
            		+ "created by programs")
	public List<String> resultSetList = new ArrayList<>();

    @Option(names = {"--site"},
    		arity = "1",
            description = "site to search")
    private SearchSite site = null;

    private File dictionaryFile;
	private InputStream dictionaryInputStream;

	private HttpClient client;
	private AbstractDownloader downloader;
	
    /** used by some non-picocli calls
     * obsolete it
     * @param cProject
     */
	public AMIDownloadTool(CProject cProject) {
		this.cProject = cProject;
	}
	
	public AMIDownloadTool() {
	}
	
    public static void main(String[] args) throws Exception {
    	new AMIDownloadTool().runCommands(args);
    }


    @Override
	protected boolean parseGenerics() {
    	makeCProjectDirectory = true;
    	super.parseGenerics();
    	if (cProject == null) {
    		throw new RuntimeException("Must give project");
    	}
    	
    	if (output == null) {
    		output = "scraped/";
    		LOG.info("set output to: " + output);
    	}
//		System.out.println("fileformats     " + rawFileFormats);
		System.out.println("project         " + cProject);
		System.out.println();
		return true;
	}


    @Override
	protected void parseSpecifics() {
		if (pageList.size() > 0 && pagesize != null) {
			limit = pageList.size() * pagesize;
		}
		normalizePageList();
		System.out.println("landingPage        " + landingPage);
		System.out.println("limit              " + limit);
		System.out.println("metadata           " + metadata);
		System.out.println("pages              " + pageList);
		System.out.println("pagesize           " + pagesize);
		System.out.println("query              " + queryList);
		System.out.println("resultSetList      " + resultSetList);
		System.out.println("site               " + site);
		System.out.println();
	}

    @Override
    protected void runSpecifics() {
		downloader = createDownloader();

		// Get and ouptut resultSets
		QueryManager queryManager = downloader.getOrCreateQueryManager();
		resultSetList = queryManager.searchAndDownloadResultSet();
		
		LandingPageManager landingPageManager = downloader.getOrCreateLandingPageManager();
		landingPageManager.downloadLandingPages();
		
		FulltextManager fulltextManager = downloader.getOrCreateFulltextManager();
		fulltextManager.downloadFullTextAndRelatedFiles();
		
		List<AbstractMetadataEntry> metadataEntryList = downloader.getMetadataEntryList();
		FullFileManager fullFileManager = downloader.getOrCreateFullFileManager();
		fullFileManager.downloadHtmlPages(metadataEntryList);
    }

	private AbstractDownloader createDownloader() {
		downloader = site.createDownloader(this);
		downloader.setCProject(cProject);
		return downloader;
	}
	
	public File getMetadataDir() {
		return new File(cProject.getOrCreateDirectory(), metadata);
	}

	public int getDownloadLimit() {
		return limit;
	}
	
	private void normalizePageList() {
		// no pages, use 1
		if (pageList.size() == 0) {
			pageList.add(1);
		}
		// first page == 0 , signal for no pages
		if (pageList.size() == 1 && pageList.get(0).equals(0)) return;
		
		// first page only, set last to first
		if (pageList.size() == 1) {
			pageList.add(pageList.get(0));
		}
		// first page <= 0, set to 1
		if (pageList.get(0) <= 0) {
			pageList.set(0,  1);
			System.err.println("page list must start from >= 1");
		}
		// upper limit less than start, set to start
		if (pageList.get(1) < pageList.get(0)) {
			System.err.println("page list out of order: "+pageList);
			pageList.set(1, pageList.get(0));
		}
	}

	public CTree getCTree(String doi) {
		CTree cTree = cProject.getExistingCTreeOrCreateNew(doi);
		cProject.add(cTree);
		return cTree;
	}

	public int getPageSize() {
		return pagesize;
	}

	public List<Integer> getPageList() {
		return pageList;
	}

	public List<String> getQueryList() {
		return queryList;
	}

	public SearchSite getSite() {
		return site;
	}

	public AbstractDownloader getDownloader() {
		return downloader;
	}
	
	public List<String> getResultsSetList() {
		return resultSetList;
	}

	public List<RawFileFormat> getRawFileFormats() {
		return rawFileFormats;
	}

}
