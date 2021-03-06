/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Powered Pixel Dungeon
 *  * Copyright (C) 2014-2020 Samuel Braithwaite
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.messages;

import java.util.Locale;

public enum Languages {
	ENGLISH("english",      "",   Status.REVIEWED,   null, null),
	
	KOREAN("한국어",         "ko", Status.OUTDATED, new String[]{"Flameblast12", "GameConqueror", "Korean2017"}, new String[]{"Cocoa", "WondarRabb1t", "ddojin0115", "eeeei", "hancyel", "linterpreteur", "lsiebnie" }),
	RUSSIAN("русский",      "ru", Status.OUTDATED, new String[]{"ConsideredHamster", "Inevielle", "yarikonline"}, new String[]{"AttHawk46", "BlueberryShortcake", "HerrGotlieb", "HoloTheWise", "Ilbko", "JleHuBbluKoT", "MrXantar", "Raymundo", "Shamahan", "apxwn", "kirusyaga", "perefrazz", "roman.yagodin", "un_logic", "Вoвa"}),
	GERMAN("deutsch",       "de", Status.OUTDATED, new String[]{"Dallukas", "KrystalCroft", "Wuzzy", "Zap0", "bernhardreiter", "davedude"}, new String[]{"Abracadabra", "Ceeee", "DarkPixel", "ErichME", "Faquarl", "LenzB", "Sarius", "SirEddi", "Sorpl3x", "ThunfischGott", "Topicranger", "apxwn", "azrdev", "carrageen", "gekko303", "johannes.schobel", "karoshi42", "koryphea", "luciocarreras", "niemand", "oragothen", "spixi"}),
	SPANISH("español",      "es", Status.OUTDATED,   new String[]{"Kiroto", "Kohru", "airman12", "grayscales"}, new String[]{"Alesxanderk", "CorvosUtopy", "Dewstend", "Dyrran", "Fervoreking", "Illyatwo2", "JPCHZ", "STKmonoqui", "alfongad", "benzarr410", "chepe567.jc", "ctrijueque", "dhg121", "javifs", "jonismack1", "tres.14159"}),
	FRENCH("français",      "fr", Status.OUTDATED, new String[]{"Emether", "Xalofar", "canc42", "kultissim", "minikrob"}, new String[]{"Alsydis", "Axce", "Basttee", "Dekadisk", "Draal", "Neopolitan", "Nyrnx", "RomTheMareep", "SpeagleZNT", "TheKappaDuWeb", "Tronche2Cake", "Ygdrazil", "antoine9298", "go11um", "levilbatard", "linterpreteur", "maeltur70", "marmous", "solthaar", "vavavoum", "speagle"}),
	CHINESE("中文",          "zh", Status.OUTDATED, new String[]{"Jinkeloid(zdx00793)"}, new String[]{"931451545", "HoofBumpBlurryface", "Lery", "Lyn_0401", "ShatteredFlameBlast", "endlesssolitude", "hmdzl001", "tempest102"}),
	POLISH("polski",        "pl", Status.OUTDATED, new String[]{"Deksippos", "kuadziw", "szymex73"}, new String[]{"Chasseur", "Darden", "KarixDaii", "MJedi", "MrKukurykpl", "Odiihinia", "Peperos", "Scharnvirk", "VasteelXolotl", "bvader95", "dusakus", "michaub", "ozziezombie", "szczoteczka22", "transportowiec96"}),
	PORTUGUESE("português", "pt", Status.OUTDATED, new String[]{"Chacal.Ex", "TDF2001", "matheus208"}, new String[]{"Bigode935", "ChainedFreaK", "Helen0903", "JST", "MadHorus", "Matie", "Tio_P_(Krampus)", "ancientorange", "danypr23", "denis.gnl", "ismael.henriques12", "mfcord", "owenreilly", "rafazago", "try31"}),
	ITALIAN("italiano",		"it", Status.OUTDATED,   new String[]{"bizzolino", "funnydwarf"}, new String[]{"4est", "DaniMare", "Danzl", "andrearubbino00", "nessunluogo", "righi.a", "umby000"}),
	CZECH("čeština",        "cs", Status.OUTDATED,   new String[]{"ObisMike"}, new String[]{"AshenShugar", "Buba237", "JStrange", "RealBrofessor", "chuckjirka"}),
	TURKISH("türkçe",       "tr", Status.OUTDATED, new String[]{"LokiofMillenium", "emrebnk"}, new String[]{"AGORAAA", "AcuriousPotato", "alpekin98", "denizakalin", "erdemozdemir98", "melezorus34", "mitux"}),
	FINNISH("suomi", 		"fi", Status.OUTDATED, new String[]{"TenguTheKnight"}, null ),
	HUNGARIAN("magyar",     "hu", Status.OUTDATED, new String[]{"dorheim", "szalaik"}, new String[]{"Navetelen", "clarovani", "dhialub", "nanometer", "nardomaa", "savarall"}),
	JAPANESE("日本語",       "ja", Status.OUTDATED, null, new String[]{"Gosamaru", "amama", "librada", "mocklike"}),
	INDONESIAN("indonésien","in", Status.OUTDATED, new String[]{"rakapratama"}, new String[]{"ZangieF347", "esprogarap"}),
	CATALAN("català",       "ca", Status.OUTDATED, new String[]{"Illyatwo2"}, new String[]{"n1ngu"}),
	BASQUE("euskara",       "eu", Status.OUTDATED,   new String[]{"Deathrevenge", "Osoitz"}, null),
	ESPERANTO("esperanto",  "eo", Status.OUTDATED, new String[]{"Verdulo"}, new String[]{"Raizin"});

	public enum Status{
		OUTDATED,//below 80% complete languages are not added.
		INCOMPLETE, //80-99% complete
		UNREVIEWED, //100% complete
		REVIEWED    //100% reviewed
	}

	private String name;
	private String code;
	private Status status;
	private String[] reviewers;
	private String[] translators;

	Languages(String name, String code, Status status, String[] reviewers, String[] translators){
		this.name = name;
		this.code = code;
		this.status = status;
		this.reviewers = reviewers;
		this.translators = translators;
	}

	public String nativeName(){
		return name;
	}

	public String code(){
		return code;
	}

	public Status status(){
		return status;
	}

	public String[] reviewers() {
		if (reviewers == null) return new String[]{};
		else return reviewers.clone();
	}

	public String[] translators() {
		if (translators == null) return new String[]{};
		else return translators.clone();
	}

	public static Languages matchLocale(Locale locale){
		return matchCode(locale.getLanguage());
	}

	public static Languages matchCode(String code){
		for (Languages lang : Languages.values()){
			if (lang.code().equals(code))
				return lang;
		}
		return ENGLISH;
	}

}
