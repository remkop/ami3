package org.contentmine.ami.tools;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.contentmine.ami.tools.AMIDictionaryTool.DictionaryFileFormat;
import org.contentmine.ami.tools.download.CurlDownloader;
import org.contentmine.graphics.html.HtmlA;
import org.contentmine.norma.NAConstants;
import org.junit.Assert;
import org.junit.Test;


/** tests AMIDictinary
 * 
 * @author pm286
 *
 */
public class AMIDictionaryTest extends AbstractAMITest {
	private static final Logger LOG = Logger.getLogger(AMIDictionaryTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	private static final File TARGET = new File("target");
	public static final File DICTIONARY_DIR = new File(TARGET, "dictionary");
	

	@Test
	public void testHelp() {
		String args = "--help";
		AMIDictionaryTool.main(args);
	}
	
	@Test
	public void testListSome() {
		String args =
				"display " +
		   " --directory src/main/resources/org/contentmine/ami/plugins/dictionary " +
           " --dictionary " + "country crispr disease"
				;
		AMIDictionaryTool.main(args);
	}
	
	@Test
	public void testCreateFungicideTerms() {
		File directory = new File("/Users/pm286/ContentMine/dictionary/dictionaries/chem");
		String dictionary = "fungicides2";
		String args =
				"create " +
				" --directory " + directory + " " +
           " --dictionary " + dictionary +
           " --outformats html,xml " +
           " --terms " + ""
           		+ "Abamectin Actinomycin Alexidine Amikacin Amphotericin B Ampicillin"
//           		+ " Anidulafungin Antimycin Aureobasidin Azithromycin Azoxystrobin Aztreonam"
//           		+ " Bacitracin Benomyl Benznidazole Bifonazole Bleomycin Boscalid"
//           		+ " Brassinin Brefeldin Calcofluor White Camptothecin Captan carbapenems Carbendazim Carbenicillin"
//           		+ " Carboxin Caspofungin Cefotaxime Ceftazidime Ceftriaxone Cefuroxime Cefuroximel Cephalexin"
//           		+ " Cercosporamide Chloramphenicol Chlorothalonil Ciprofloxacin Closantel Colistin Copper "
//           		+ " Copper_sulphate Cycloheximide Cyclosporine Cyproconazole Daptomycin Diethofencarb"
//           		+ " Difenoconazole Diniconazole Doxycycline Eflornithine Emamectin Epoxyconazole"
//           		+ " Ethambutol Farnesol Fenarimol Fenhexamid Fenpropidin Fluconazole Flucytosine"
//           		+ " Fludioxonil Flutriafol Gentamicin Gramicidin Hydrogen_peroxide Hygromycin"
//           		+ " Hymexazol Imipenem Iprodione Isoniazid Itraconazole Ketoconazole Latrunculin"
//           		+ " Leptomycin B Lincomycin Linezolid Mancozeb Mecillinam Meropenem Micafungin"
//           		+ " Miconazole Miltefosine Moxifloxacin Myriocin Naftifine Nalidixic_acid Neomycin"
//           		+ " Nifurtimox Nikkomycin Nisin nitrofurantoin Norfloxacin Novobiocin Nystatin"
//           		+ " Oligomycin Oxacillin Oxolinic_acid Oxytetracycline Paromomycin Penicillin"
//           		+ " Pentamidine Phenamacril phosphomycin Plumbagin Polymyxin_B1 posaconazole"
//           		+ " Prochloraz Propiconazole Pyrimethanil Rapamycin Resveratrol Rifampicin"
//           		+ " Rifampin Rose Bengal Rotenone Salicylhydroxamic Sordarin Spectinomycin"
//           		+ " Spiroxamine Streptomycin sulfate Strobilurin Sulbactam Tebuconazole"
//           		+ " Teicoplanin Telithromycin terbinafine Tetracycline Thiabendazole Thiophanate-methyl"
//           		+ " Tiamulin Ticarcillin Tigecycline Tobramycin Triadimefon Trichostatin Triclabendazole"
//           		+ " Triclosan Tricyclazole Tridemorph Trimethoprim Tunicamycin Tylosin Valinomycin Vancomycin"
           		+ " Verapamil Vinclozolin Virginiamycin Voriconazole"
				;
		AMIDictionaryTool.main(args);
//		Assert.assertTrue(""+directory, new directory.exists());
	}
	
	@Test
	public void testCreateFungicideTermsFromFile() throws IOException {
		File directory = new File("/Users/pm286/ContentMine/dictionary/dictionaries/chem");
		String dictionary = "fungicides2a";
		List<String> fungicides = Arrays.asList(new String[] {
		   	"Abamectin",
		   	"Actinomycin",
		   	"Virginiamycin",
		   	"Voriconazole"
		   	}
		);
		File fungicideFile = new File("target/fungicides/fungicides.txt");
		FileUtils.writeLines(fungicideFile, fungicides);
		
		String args =
				"create " +
				" --directory " + directory + " " +
           " --dictionary " + dictionary +
           " --outformats html,xml " +
           " --termfile " + fungicideFile
				;
		AMIDictionaryTool.main(args);
		File dictionaryFile = new File(directory, dictionary+"."+"xml");
		Assert.assertTrue(""+dictionaryFile, dictionaryFile.exists());
	}
	

	@Test
	public void testWikipediaTables() throws IOException {
		String dict = " chem.protpredict";
		String args =
			"create " +
           " --input https://en.wikipedia.org/wiki/List_of_protein_structure_prediction_software " +
           " --informat wikitable " +
           " --namecol Name " +
			"--linkcol Name " +
           " --urlcol Link " +
           " --outformats xml,json,html " +
           " --dictionary " +dict
			;
		AbstractAMITool amiDictionary = new AMIDictionaryTool();
		amiDictionary.runCommands(args);
//		XMLUtil.debug(amiDictionary.getSimpleDictionary(), new File(DICTIONARY_DIR, dict+".html"), 1);
		
	}
	
	@Test
	public void testWikipediaTables2() throws IOException {
		String dictname = "socialnetwork";
		String dict = "soc." + dictname;
		File dictFile = new File(DICTIONARY_DIR, dictname+".xml");
		String args =
			"create " +
           " --input https://en.wikipedia.org/wiki/List_of_social_networking_websites " +
           " --informat wikitable " +
           " --namecol Name " +
			"--linkcol Name " +
           " --outformats xml,json,html " +
           " --dictionary "+dictname;
		AbstractAMITool amiDictionary = new AMIDictionaryTool();
		amiDictionary.runCommands(args);
		Assert.assertTrue(""+dictFile, dictFile.exists());

	}
	
	@Test
	public void testWikipediaPage() throws IOException {
		String dict = "proteinStructure";
		File dictFile = new File(DICTIONARY_DIR, dict+".xml");
		String args =
			"create " +
           " --input https://en.wikipedia.org/wiki/Protein_structure " +
           " --informat wikipage " +
           " --outformats xml,json,html " +
           " --dictionary " +dict;
//		new AMIDictionaryTool().runCommands(args);
		Assert.assertTrue(""+dictFile, dictFile.exists());
	}

	@Test
	// LONG
	public void testWikipediaPageAedesIT() throws IOException {
		String dict = "animal.aedes";
		String args =
			"create " +
           " --input https://en.wikipedia.org/wiki/Aedes_aegypti " +
           " --informat wikipage " +
		   " --dictionary " +dict +
           " --outformats xml,json,html " +
           " --directory " +DICTIONARY_DIR.toString()
			;
		new AMIDictionaryTool().runCommands(args);
	}

	@Test
	// LONG
	public void testWikipediaChildhoodObesityIT() throws IOException {
		String dict = "med.childhoodobesity";
		String col = "Condition";
		String args =
			"create " +
           " --input https://en.wikipedia.org/wiki/Childhood_obesity " +
           " --informat wikitable" +
           " --namecol " + col +
		   " --dictionary " + dict +
           " --outformats xml,json,html " +
           " --directory " +DICTIONARY_DIR.toString()
			;
		// ami-dictionaries create -i https://en.wikipedia.org/wiki/Aedes_aegypti --informat wikipage --hreftext --dictionary aedes0 --outformats xml --directory ~/ContentMine/dictionary/
		new AMIDictionaryTool().runCommands(args);
	}

	@Test
	public void testWikipediaPageReindeerIT() throws IOException {
		String dict = "animal.reindeer";
		String args =
			"create " +
           " --input https://en.wikipedia.org/wiki/Category:Reindeer " +
			"--informat wikipage " +
			"--outformats xml,json,html " +
           " --dictionary " + dict +
           " --directory " +DICTIONARY_DIR.toString()
			;
		new AMIDictionaryTool().runCommands(args);
	}
	@Test
	// LONG
	public void testWikipediaPageMonoterpenesIT() throws IOException {
		String dict = "chem.monoterpenes";
		String args =
			"create " +
           " --input https://en.wikipedia.org/wiki/Category:Monoterpenes " +
			"--informat wikipage " +
			"--outformats xml,json,html " +
           " --dictionary " + dict +
           " --directory " +DICTIONARY_DIR.toString()
			;
		new AMIDictionaryTool().runCommands(args);
	}

	@Test
	public void testWikipediaNTDs() throws IOException {
		String dict = "med.ntd";
		String whoCol = ".*WHO.*CDC.*";
		String args =
			"create " +
           " --input https://en.wikipedia.org/wiki/Neglected_tropical_diseases " +
           " --informat wikitable " +
           " --namecol " +whoCol +
           " --linkcol " +whoCol +
           " --base http://en.wikipedia.org " +
           " --outformats xml,json,html " +
           " --dictionary " +dict +
           " --directory " +DICTIONARY_DIR.toString()
			;
		new AMIDictionaryTool().runCommands(args);
	}
	
	@Test
	public void testWikipediaNTDPLOS() throws IOException {
		String dict = "med.ntd1";
		String searchCol = "PLOS.*";
		String args =
			"create " +
           " --input https://en.wikipedia.org/wiki/Neglected_tropical_diseases " +
           " --informat wikitable " +
           " --namecol " +searchCol +
           " --linkcol " +searchCol +
           " --dictionary " + dict +
           " --outformats xml,json,html " +
           " --directory " +DICTIONARY_DIR.toString()
			;
		new AMIDictionaryTool().runCommands(args);
	}
	
	@Test
	public void testWikipediaHumanInsectVectorsIT() throws IOException {
		String dict = "animal.insectvectorshuman";
		String args =
			"create " +
           " --input https://en.wikipedia.org/wiki/Category:Insect_vectors_of_human_pathogens " +
           " --informat wikicategory " +
           " --dictionary " +dict +
           " --outformats xml,json,html " +
           " --directory " +DICTIONARY_DIR.toString()
			;
		new AMIDictionaryTool().runCommands(args);
	}
	
	
	@Test
	public void testWikipediaOcimumIT() throws IOException {
		String dict = "plants.ocimumten";
		String args =
			"create " +
           " --input https://en.wikipedia.org/wiki/Ocimum_tenuiflorum " +
           " --informat wikipage " +
           " --hreftext " + // currently needed to enforce use of names
			"--dictionary " +dict +
           " --outformats xml,json,html " +
           " --directory " +DICTIONARY_DIR.toString()
			;
		new AMIDictionaryTool().runCommands(args);
	}
	
	@Test
	public void testCreateFromTerms() {
		String dict = "phys.crystalsystem";
		String args =
			"create " +
           " --terms cubic,tetragonal,hexagonal,trigonal,orthorhombic,monoclinic,triclinic " +
           " --dictionary " +dict +
           " --directory " +DICTIONARY_DIR.toString();
		new AMIDictionaryTool().runCommands(args);
	}
	
	@Test
	public void testReadMammalsCSV() throws IOException {
		File mammalsCSV = new File(NAConstants.PLUGINS_DICTIONARY_DIR, "EDGEMammalsSmall.csv");
		
		String dict = "animal.edgemammals";
		String args =
			"create " +
           " --input " +mammalsCSV.getAbsolutePath() +
           " --informat csv " +
           " --termcol Species " +
           " --namecol Common names " +
           " --hrefcols IUCN Red List link " +
           " --datacols ED Score,GE Score " +
           " --dictionary " +dict +
           " --outformats xml,html,json " +
           " --directory " +DICTIONARY_DIR.toString() +
           " --booleanquery"
			;
		new AMIDictionaryTool().runCommands(args);
		
	}

	@Test
	public void testWikipediaConservation1() throws IOException {
		String dict = "bio.conservation";
		String args =
			"create " +
           " --hreftext " + // currently needed to enforce use of names
			"--input https://en.wikipedia.org/wiki/Conservation_biology " +
           " --informat wikipage " +
           " --dictionary " +dict +
           " --directory target/dictionary " +
           " --outformats " +DictionaryFileFormat.xml.toString() +
           " --log4j org.contentmine.ami.lookups.WikipediaDictionary INFO " +
           " --log4j org.contentmine.norma.input.html.HtmlCleaner INFO"
			;
		new AMIDictionaryTool().runCommands(args);
	}

	@Test
	public void testWikipediaIndianSpice() throws IOException {
		String dict = "plants.spice";
		String searchCol = "Standard English";
		String args =
			"create " +
           " --input https://en.wikipedia.org/wiki/List_of_Indian_spices " +
           " --informat wikitable " +
           " --namecol " +searchCol +
           " --dictionary " +dict +
           " --outformats xml,json,html " +
           " --directory " +DICTIONARY_DIR.toString()
			;
		new AMIDictionaryTool().runCommands(args);
	}
	
	@Test
	public void testWikipediaWikipage() throws IOException {
		String dict = "plants.virus";
		String args =
			"create " +
           " --hreftext " + // currently needed to enforce use of names
			"--input https://en.wikipedia.org/wiki/Plant_virus " +
           " --informat wikipage " +
           " --dictionary " +dict +
           " --outformats html,json,xml " +
           " --wikilinks"
			;
		new AMIDictionaryTool().runCommands(args);
	}

	@Test
	public void testWikipediaWikiTemplateOnline() throws IOException {
		String dict = "respiratory_pathology";
		
		String args =
			"create " +
		   " --input https://en.wikipedia.org/wiki/Template:Respiratory_pathology " +
           " --informat wikitemplate " +
           " --dictionary " + dict +
           " --outformats html,xml " +
           " --wikilinks"
			;
		new AMIDictionaryTool().runCommands(args);
//		Assert.assertTrue("file exists "+dictionaryFile, dictionaryFile.exists());
	}

	@Test
	public void testWikipediaWikiTemplate() throws IOException {
		String dict = "respiratory_pathology";
/** this shows how awful the Mediawiki markup is; a mixture of table and dictionary
 * the leaf nodes seem all to  be <dd>< href="...">...</a></dd> and this iw what we'll use
 * 
 * the other links are not leafs and not dictionary items.
 * that may vary in other templates		
 */
		String htmlS = ""+
"<tr>" +
"<th scope='row' class='navbox-group' style='width:1%'><a href='https://en.wikipedia.org/wiki/Human_head' title='Human head'>Head</a></th>" +
"<td class='navbox-list navbox-odd' style='text-align:left;border-left-width:2px;border-left-style:solid;width:100%;padding:0px'>" +
"<div style='padding:0em 0.25em'>" +
"<dl>" +
"<dt><i><a href='https://en.wikipedia.org/wiki/Paranasal_sinuses' title='Paranasal sinuses'>sinuses</a></i></dt>" +
"<dd></dd>" +
"<dd><a href='https://en.wikipedia.org/wiki/Sinusitis' title='Sinusitis'>Sinusitis</a></dd>" +
"</dl>" +
"<dl>" +
"<dt><i><a href='https://en.wikipedia.org/wiki/Human_nose' title='Human nose'>nose</a></i></dt>" +
"<dd></dd>" +
"<dd><a href='https://en.wikipedia.org/wiki/Rhinitis' title='Rhinitis'>Rhinitis</a>" +
"<dl>" +
"<dd><a href='https://en.wikipedia.org/wiki/Nonallergic_rhinitis' title='Nonallergic rhinitis'>Vasomotor rhinitis</a></dd>" +
"<dd><a href='https://en.wikipedia.org/wiki/Chronic_atrophic_rhinitis' title='Chronic atrophic rhinitis'>Atrophic rhinitis</a></dd>" +
"<dd><a href='https://en.wikipedia.org/wiki/Allergic_rhinitis' title='Allergic rhinitis'>Hay fever</a></dd>" +
"</dl>" +
"</dd>" +
"<dd><a href='https://en.wikipedia.org/wiki/Nasal_polyp' title='Nasal polyp'>Nasal polyp</a></dd>" +
"<dd><a href='https://en.wikipedia.org/wiki/Rhinorrhea' title='Rhinorrhea'>Rhinorrhea</a></dd>" +
"<dd><i><a href='https://en.wikipedia.org/wiki/Nasal_septum' title='Nasal septum'>nasal septum</a></i>" +
"<dl>" +
"<dd><a href='https://en.wikipedia.org/wiki/Nasal_septum_deviation' title='Nasal septum deviation'>Nasal septum deviation</a></dd>" +
"<dd><a href='https://en.wikipedia.org/wiki/Nasal_septum_perforation' title='Nasal septum perforation'>Nasal septum perforation</a></dd>" +
"<dd><a href='https://en.wikipedia.org/wiki/Nasal_septal_hematoma' title='Nasal septal hematoma'>Nasal septal hematoma</a></dd>" +
"</dl>" +
"</dd>" +
"</dl>" +
"<dl>" +
"<dt><i><a href='https://en.wikipedia.org/wiki/Tonsil' title='Tonsil'>tonsil</a></i></dt>" +
"<dd></dd>" +
"<dd><a href='https://en.wikipedia.org/wiki/Tonsillitis' title='Tonsillitis'>Tonsillitis</a></dd>" +
"<dd><a href='https://en.wikipedia.org/wiki/Adenoid_hypertrophy' title='Adenoid hypertrophy'>Adenoid hypertrophy</a></dd>" +
"<dd><a href='https://en.wikipedia.org/wiki/Peritonsillar_abscess' title='Peritonsillar abscess'>Peritonsillar abscess</a></dd>" +
"</dl>" +
"</div>" +
"</td>" +
"</tr>" +
"";
		htmlS = htmlS.replaceAll(" ", "%20"); // escape with percentEncoding
		
		String args =
			"create " +
//		   " --input https://en.wikipedia.org/wiki/Template:Respiratory_pathology " +
           " --informat wikitemplate " +
           " --dictionary " + dict +
           " --outformats html,xml " +
           " --testString " + htmlS + " " +
           " --wikilinks wikidata"
			;
		new AMIDictionaryTool().runCommands(args);
//		Assert.assertTrue("file exists "+dictionaryFile, dictionaryFile.exists());
	}
	
	@Test
	public void testCreateFromWPTemplateFile() {
		String fileTop = "/Users/pm286/projects/openVirus";
		String dict = "viral_systemic_disease";
		String fileroot = fileTop + "/" + "dictionaries"+ "/" + dict;
		String type = "html";
		File dictionaryDir = new File(new File(fileroot), type);
		String htmlFilename = fileroot + "." + type;
		String args =
				"create " +
			   " --input " + htmlFilename +
			   " --informat wikitemplate " +
	           " --dictionary " + dict +
	           " --directory " + dictionaryDir +
	           " --outformats html,xml " +
	           " --wikilinks wikidata"
				;
			new AMIDictionaryTool().runCommands(args);
	}
	
	@Test
	public void testDownloadMediawiki() throws IOException {
		downloadAndTest(new File("target/curl.mw.txt"), 
				"https://en.wikipedia.org/w/index.php?title=Template:Viral_systemic_diseases", 50000, 51000);
		downloadAndTest(new File("target/curl.mw.edit.txt"), 
				"https://en.wikipedia.org/w/index.php?title=Template:Viral_systemic_diseases&action=edit", 44000, 45000);
	}
	
	@Test
	public void testCreateFromMediawikiTemplateURL() {
		String fileTop = "/Users/pm286/projects/openVirus/dictionaries/";
		String wptype = "mwk";
		String template = "Viral_systemic_diseases";
		createFromMediaWikiTemplate(template, fileTop, wptype);
	}
	
	@Test
	public void testCreateFromMediawikiTemplateListURLOld() {
		String[] templates = {"Baltimore_(virus_classification)", "Antiretroviral_drug", "Virus_topics"};
		String fileTop = "/Users/pm286/projects/openVirus/dictionaries/";
		String wptype = "mwk";
		for (String template : templates) {
			createFromMediaWikiTemplate(template, fileTop, wptype);
		}
	}

	@Test
	public void testCreateFromMediawikiTemplateListURL() {
		String dictionaryTop = "/Users/pm286/projects/openVirus/dictionaries/";
		String wptype = "mwk";
		String args =
				"create " +
		       " --directory " + dictionaryTop +
		       " --outformats html,xml " +
		       " --template " + " Virus_topics Baltimore_(virus_classification) Antiretroviral_drug" +
		       " --wptype " + wptype +
		       " --wikilinks wikidata"
				;
		new AMIDictionaryTool().runCommands(args);
	}

	@Test
	public void testCreateVirusesFromTerms() {
		String dict = "plants.viruses";
		String args =
			"create " +
           " --terms Cucumovirus,Tymovirus,Bromovirus,Potexvirus,Ilarvirus,Nepovirus,Carmovirus,Potyvirus,Potyvirus,"
					+ "Badnavirus,Tymovirus,Tobravirus,Closterovirus,Necrovirus,TNsatV-like satellite,Nepovirus,Nepovirus,"
					+ "Nepovirus,Ilarvirus,Comovirus,Dianthovirus,Carlavirus,Sobemovirus,Caulimovirus,Enamovirus,Cytorhabdovirus,"
					+ "Ophiovirus,Cocadviroid,Aureusvirus,Ilarvirus,Bromovirus,Tungrovirus,Waikavirus,Sobemovirus,Alphacryptovirus,"
					+ "Potyvirus,Potexvirus,Tospovirus,Cavemovirus,Potyvirus,Carlavirus,Mastrevirus,Petuvirus,Potexvirus,Carmovirus,"
					+ "Carmovirus,Nucleorhabdovirus,Ampelovirus " +
           " --dictionary " +dict +
           " --directory " +DICTIONARY_DIR.toString() +
           " --wikilinks wikidata"
          ;
		new AMIDictionaryTool().runCommands(args);
	}
	
	@Test
	public void testTranslateJSONtoXML() {
		String args =
			"translate " +
           " --directory src/test/resources/org/contentmine/ami/dictionary " +
           " --dictionary alliaceae.json buxales.json " +
           " --outformats xml"
		;
		new AMIDictionaryTool().runCommands(args);
	}
	
	@Test
	public void testTranslateJSONtoXMLAbsolute() {
		String args =
			"translate" +
           " --dictionary src/test/resources/org/contentmine/ami/dictionary/alliaceae.json " +
			                "src/test/resources/org/contentmine/ami/dictionary/buxales.json " +
           " --outformats xml"
		;
		new AMIDictionaryTool().runCommands(args);
	}
	
	@Test
	public void testTranslateJSONtoXMLAbsoluteWikidata() {
		String args =
			"translate " +
           " --dictionary src/test/resources/org/contentmine/ami/dictionary/alliaceae.json " +
			                "src/test/resources/org/contentmine/ami/dictionary/buxales.json " +
           " --outformats xml " +
           " --wikilinks wikidata wikipedia"
		;
		new AMIDictionaryTool().runCommands(args);
	}
	
	@Test
	public void testWikidataLookup() {
		String dict = "plants.misc";
		String args =
			"create " +
           " --terms Buxus sempervirens " +
           " --dictionary " + dict +
           " --directory " + DICTIONARY_DIR.toString() +
           " --wikilinks wikidata"
			;
		new AMIDictionaryTool().runCommands(args);
	}

	@Test
	public void testAmbarishBugWikipediaDictionaryCreation() {
		String dict = "plants.misc";
		String args =
			"create " +
           " --terms Buxus sempervirens " +
           " --dictionary " +dict +
           " --directory " + DICTIONARY_DIR.toString() +
           " --wikilinks wikidata"
			;
		new AMIDictionaryTool().runCommands(args);
	}

	@Test
	/** LONG! */
	public void testListOfRiceVarieties() {
		String args = "create"
				+ " --input https://en.wikipedia.org/wiki/List_of_rice_varieties"
				+ " --informat wikipage"
//				+ " --hreftext"
				+ " --dictionary ricevarieties"
				+ " --outformats xml,json,html";
		new AMIDictionaryTool().runCommands(args);
	}
	
	@Test
	public void testWikipediaPageOcimum() {
		String args = "create"
				+ " --informat wikipage"
				+ " --hreftext"
				+ " --input https://en.wikipedia.org/wiki/Ocimum_tenuiflorum"
				+ " --dictionary otenuiflorum"
				+ " --directory mydictionaries"
				+ " --outformats xml,html";
		new AMIDictionaryTool().runCommands(args);

	}
	@Test
	public void testDictionarySearch() {
		String args = "search"
				+ " --dictionary "+CEV+"/dictionary/compound/compound.xml"
//				+ " --search thymol carvacrol"
				+ " --searchfile "+CEV_SEARCH+"/oil186/__tables/compound_set.txt"
				+ "";
		new AMIDictionaryTool().runCommands(args);

	}

	@Test
	public void testMediaWikiTemplate() {
		String mw = ""
				+ "{{Navbox\n" + 
				" | name = Viral systemic diseases\n" + 
				" | title = [[Infection|Infectious diseases]] – [[Viral disease|viral systemic diseases]] ([[ICD-10 Chapter I: Certain infectious and parasitic diseases#A80–B34 – Viral infections|A80–B34]], [[List of ICD-9 codes 001–139: infectious and parasitic diseases#Human immunodeficiency virus (HIV) infection (042–044)|042–079]])\n" + 
				" | state = {{{state<includeonly>|autocollapse</includeonly>}}}\n" + 
				" | listclass = hlist\n" + 
				"\n" + 
				" | group1 = [[Oncovirus]]\n" + 
				" | list1 =\n" + 
				"; [[DNA virus]]\n" + 
				": ''[[Hepatitis B virus|HBV]]''\n" + 
				":: [[Hepatocellular carcinoma]]\n" + 
				": ''[[Papillomaviridae|HPV]]''\n" + 
				":: [[Cervical cancer]]\n" + 
				":: [[Anal cancer]]\n" + 
				":: [[Penile cancer]]\n" + 
				":: [[Vulvar cancer]]\n" + 
				":: [[Vaginal cancer]]\n" + 
				":: [[HPV-positive oropharyngeal cancer|Oropharyngeal cancer]]\n" + 
				": ''[[Kaposi's sarcoma-associated herpesvirus|KSHV]]''\n" + 
				":: [[Kaposi's sarcoma]]\n" + 
				": ''[[Epstein–Barr virus|EBV]]''\n" + 
				":: [[Nasopharyngeal carcinoma]]\n" + 
				":: [[Burkitt's lymphoma]]\n" + 
				":: [[Hodgkin's lymphoma]]\n" + 
				":: [[Follicular dendritic cell sarcoma]]\n" + 
				":: [[Extranodal NK/T-cell lymphoma, nasal type]]\n" + 
				": ''[[Merkel cell polyomavirus|MCPyV]]''\n" + 
				":: [[Merkel-cell carcinoma]]\n" + 
				"\n" + 
				"; [[RNA virus]]\n" + 
				": ''[[Hepacivirus C|HCV]]''\n" + 
				":: [[Hepatocellular carcinoma]]\n" + 
				":: [[Splenic marginal zone lymphoma]]\n" + 
				": ''[[Human T-lymphotropic virus 1|HTLV-I]]''\n" + 
				":: [[Adult T-cell leukemia/lymphoma]]\n" + 
				"\n" + 
				" | group2 = [[Immune disorder]]s\n" + 
				" | list2 =\n" + 
				"* ''[[HIV]]''\n" + 
				"** [[HIV/AIDS|AIDS]]\n" + 
				"\n" + 
				" | group3 = [[Central nervous system viral disease|Central<br /> nervous system]]\n" + 
				" | list3 = {{Navbox|subgroup\n" + 
				"\n" + 
				"   | group1 = [[Viral encephalitis|Encephalitis]]/<br />[[Viral meningitis|meningitis]]\n" + 
				"   | list1 =\n" + 
				"; [[DNA virus]]\n" + 
				": ''[[Human polyomavirus 2]]''\n" + 
				":: [[Progressive multifocal leukoencephalopathy]]\n" + 
				"\n" + 
				"; [[RNA virus]]\n" + 
				": ''[[Measles morbillivirus|MeV]]''\n" + 
				":: [[Subacute sclerosing panencephalitis]]\n" + 
				": ''[[Lymphocytic choriomeningitis|LCV]]''\n" + 
				":: [[Lymphocytic choriomeningitis]]\n" + 
				": [[Arbovirus encephalitis]]\n" + 
				": ''[[Orthomyxoviridae]]'' (probable)\n" + 
				":: [[Encephalitis lethargica]]\n" + 
				": ''[[Rabies virus|RV]]''\n" + 
				":: [[Rabies]]\n" + 
				": [[Chandipura vesiculovirus]]\n" + 
				": [[Herpesviral meningitis]]\n" + 
				": [[Ramsay Hunt syndrome type 2]]\n" + 
				"\n" + 
				"   | group2 = [[Myelitis]]\n" + 
				"   | list2 =\n" + 
				"* ''[[Poliovirus]]''\n" + 
				"** [[Polio|Poliomyelitis]]\n" + 
				"** [[Post-polio syndrome]]\n" + 
				"* ''[[Human T-lymphotropic virus 1|HTLV-I]]''\n" + 
				"** [[Tropical spastic paraparesis]]\n" + 
				"\n" + 
				"   | group3 = [[Eye disease|Eye]]\n" + 
				"   | list3 =\n" + 
				"* ''[[Cytomegalovirus]]''\n" + 
				"** [[Cytomegalovirus retinitis]]\n" + 
				"* ''[[Herpes simplex virus|HSV]]''\n" + 
				"** [[Herpes simplex keratitis|Herpes of the eye]]\n" + 
				"\n" + 
				" }}\n" + 
				"\n" + 
				" | group4 = [[Cardiovascular disease|Cardiovascular]]\n" + 
				" | list4 =\n" + 
				"* ''[[Coxsackie B virus|CBV]]''\n" + 
				"** [[Pericarditis]]\n" + 
				"** [[Myocarditis]]\n" + 
				"\n" + 
				" | group5 = [[Respiratory system]]/<br />[[Common cold|acute viral nasopharyngitis]]/<br />[[viral pneumonia]]\n" + 
				" | list5 = {{Navbox|subgroup\n" + 
				"\n" + 
				"   | group1 = [[DNA virus]]\n" + 
				"   | list1 =\n" + 
				"* ''[[Epstein–Barr virus]]''\n" + 
				"** [[Epstein–Barr virus infection|EBV infection]]/[[Infectious mononucleosis]]\n" + 
				"* ''[[Cytomegalovirus]]''\n" + 
				"\n" + 
				"   | group2 = [[RNA virus]]\n" + 
				"   | list2 =\n" + 
				"* [[RNA virus#Group IV – positive-sense ssRNA viruses|IV]]: ''[[Severe acute respiratory syndrome coronavirus|SARS coronavirus]]''\n" + 
				"** [[Severe acute respiratory syndrome]]\n" + 
				"* ''[[Middle East respiratory syndrome-related coronavirus|MERS coronavirus]]''\n" + 
				"** [[Middle East respiratory syndrome]]\n" + 
				"* ''[[Severe acute respiratory syndrome coronavirus 2|SARS coronavirus 2]]''\n" + 
				"** [[Coronavirus disease 2019]]\n" + 
				"\n" + 
				"* [[RNA virus#Group V – negative-sense ssRNA viruses|V]]: ''[[Orthomyxoviridae]]: [[Influenza A virus|Influenza virus A]]/[[Influenza B virus|B]]/[[Influenza C virus|C]]/[[Influenza D virus|D]]''\n" + 
				"** [[Influenza]]/[[Avian influenza]]\n" + 
				"\n" + 
				"* V, ''[[Paramyxoviridae]]: [[Human parainfluenza viruses]]''\n" + 
				"** [[Human parainfluenza viruses|Parainfluenza]]\n" + 
				"* ''[[Human orthopneumovirus]]''\n" + 
				"* ''[[Human metapneumovirus|hMPV]]''\n" + 
				"\n" + 
				" }}\n" + 
				"\n" + 
				" | group6 = [[Human digestive system]]\n" + 
				" | list6 = {{Navbox|subgroup\n" + 
				"\n" + 
				"   | group1 = [[Pharynx]]/[[Esophagus]]\n" + 
				"   | list1 =\n" + 
				"* ''[[Mumps rubulavirus|MuV]]''\n" + 
				"** [[Mumps]]\n" + 
				"* ''[[Cytomegalovirus]]''\n" + 
				"** [[Cytomegalovirus esophagitis]]\n" + 
				"\n" + 
				"   | group2 = [[Gastroenteritis#Viral|Gastroenteritis]]/<br />[[Gastroenteritis#Virus|diarrhea]]\n" + 
				"   | list2 =\n" + 
				"; [[DNA virus]]\n" + 
				": ''[[Adenoviridae|Adenovirus]]''\n" + 
				":: [[Adenovirus infection]]\n" + 
				"\n" + 
				"; [[RNA virus]]\n" + 
				": ''[[Rotavirus]]''\n" + 
				": ''[[Norovirus]]''\n" + 
				": ''[[Astrovirus]]''\n" + 
				": ''[[Coronavirus]]''\n" + 
				"\n" + 
				"   | group3 = [[Viral hepatitis|Hepatitis]]\n" + 
				"   | list3 =\n" + 
				"; [[DNA virus]]\n" + 
				": ''[[Hepatitis B virus|HBV]]'' ([[Hepatitis B|B]])\n" + 
				"\n" + 
				"; [[RNA virus]]\n" + 
				": ''[[Coxsackie B virus|CBV]]''\n" + 
				": ''[[Hepatitis A#Virology|HAV]]'' ([[Hepatitis A|A]])\n" + 
				": ''[[Hepacivirus C|HCV]]'' ([[Hepatitis C|C]])\n" + 
				": ''[[Hepatitis D|HDV]]'' ([[Hepatitis D|D]])\n" + 
				": ''[[Orthohepevirus A|HEV]]'' ([[Hepatitis E|E]])\n" + 
				": ''[[GB virus C|HGV]]'' ([[GB virus C|G]])\n" + 
				"\n" + 
				"   | group4 = [[Pancreatitis]]\n" + 
				"   | list4 =\n" + 
				"* ''[[Coxsackie B virus|CBV]]''\n" + 
				"\n" + 
				" }}\n" + 
				"\n" + 
				" | group7 = [[Viral skin disease|Skin]] and<br /> [[mucous membrane]]<br />[[lesion]]s,<br /> including [[exanthem]]\n" + 
				"\n" + 
				" | group8 = [[Genitourinary system|Urogenital]]\n" + 
				" | list8 =\n" + 
				"* ''[[BK virus]]''\n" + 
				"* ''[[Mumps rubulavirus|MuV]]''\n" + 
				"** [[Mumps]]\n" + 
				"\n" + 
				"}}<noinclude>\n" + 
				"{{Documentation}}\n" + 
				"</noinclude>"
				+ "";
		
		List<HtmlA> aList = AMIDictionaryTool.parseMediaWiki(mw);
		Assert.assertEquals("aList "+aList.size(), 140, aList.size());
	}
	
	// PRIVATE
	private void downloadAndTest(File outputFile, String urlString, int minsize, int maxsize) throws IOException {
		new CurlDownloader().setUrlString(urlString).setOutputFile(outputFile).run();
		Assert.assertTrue("outputfile exists "+outputFile, outputFile.exists());
		long sizeOf = FileUtils.sizeOf(outputFile);
		Assert.assertTrue("outputfile size "+sizeOf+" / "+outputFile, sizeOf > minsize && sizeOf < maxsize);
	}

	private void createFromMediaWikiTemplate(String template, String fileTop, String wptype) {
		String args =
				"create " +
	           " --directory " + fileTop +
	           " --outformats html,xml " +
	           " --template " + template +
	           " --wptype " + wptype +
	           " --wikilinks wikidata"
				;
		new AMIDictionaryTool().runCommands(args);
	}
	



}
