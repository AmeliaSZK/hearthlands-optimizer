package hearthlandsoptimizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static final String TEST_DATA = "name;type;west;east;south;north;class;staff;loads;consumption;production\n"
            + "Apothecary;APOTHECARY;1;1;1;1;other;8;24; ;\n"
            + "Aviary;AVIARY;1;0;1;0;prod;8;24; ;1 EGGS 1 FEATHERS\n"
            + "Bakery;BAKERY;1;0;1;0;prod;10;24;1 FLOUR 1 LOGS 1 WATER;1 BREAD\n"
            + "Beekeeper;BEEKEEPER;0;0;0;1;prod;12;24;2 FLOWERS;1 HONEY\n"
            + "Beer Brewery;BEER_BREWERY;1;0;0;0;prod;12;24;1 HOPS 1 GRAIN 1 LOGS;1 BEER\n"
            + "Carpenter;CARPENTER;1;1;1;1;prod;10;24;1 PLANKS;1 FURNITURE\n"
            + "Cider Maker;CIDER_BREWERY;0;1;0;0;prod;18;24;1 APPLES 1 LOGS 1 WATER;1 CIDER\n"
            + "Clothier;CLOTHIER;1;1;1;1;prod;6;24;1 TEXTILE;1 CLOTHES\n"
            + "Coal Digger;COAL_DIGGER;1;1;1;1;prod;8;12; ;1 COAL\n"
            + "Coal Mine;COAL_MINE;1;1;1;1;prod;24;12;;1 COAL\n"
            + "Dairy;DIARY;0;1;0;0;prod;8;24;1 MILK;1 CHEESE\n"
            + "Fishing Quay;FISHING_QUAY;0;0;0;1;prod;12;24; ;1 FISH\n"
            + "Flowers Forestry;FORESTER;1;1;1;1;prod;4;48; ;1 FLOWERS\n"
            + "Mushroom Forestry;FORESTER;1;1;1;1;prod;4;48; ;1 MUSHROOMS\n"
            + "Tree Forestry;FORESTER;1;1;1;1;prod;4;48; ;1 TREES\n"
            + "Gold Digger;GOLD_DIGGER;1;0;1;0;prod;12;12; ;1 GOLD\n"
            + "Gold Mine;GOLD_MINE;1;0;1;0;prod;40;12;;1 GOLD\n"
            + "Apple Grower;GROWER;1;1;1;0;prod;8;24; ;1 APPLES\n"
            + "Cotton Grower;GROWER;1;1;1;0;prod;8;24; ;1 COTTON\n"
            + "Grain Grower;GROWER;1;1;1;0;prod;8;24; ;1 GRAIN\n"
            + "Turnip Grower;GROWER;1;1;1;0;prod;8;24; ;1 TURNIPS\n"
            + "Herbalist;HERBALIST;1;1;1;1;prod;12;24;1 MUSHROOMS 1 LOGS;1 MEDICINE\n"
            + "Any Hunter;HUNTER;0;1;1;1;prod;12;12; ;1 MEAT\n"
            + "Boar Hunter;HUNTER;0;1;1;1;prod;12;12; ;1 MEAT 1 LEATHER\n"
            + "Rabbit Hunter;HUNTER;0;1;1;1;prod;12;12; ;1 MEAT 1 WOOL\n"
            + "Turkey Hunter;HUNTER;0;1;1;1;prod;12;12; ;1 MEAT 1 FEATHERS\n"
            + "Iron Digger;IRON_DIGGER;1;1;1;1;prod;12;12; ;1 IRON_ORE\n"
            + "Iron Mine;IRON_MINE;1;1;1;1;prod;32;12;;1 IRON_ORE\n"
            + "Marketplace;MARKETPLACE;1;1;1;1;other;12;24; ;\n"
            + "Masonry;MASONRY;1;1;1;1;prod;8;24; ;\n"
            + "Mead Brewer;MEAD_BREWERY;0;0;0;1;prod;8;24;1 HONEY;1 MEAD\n"
            + "Mill;MILL;1;0;1;0;prod;4;24;1 GRAIN;1 FLOUR\n"
            + "Mint Diggers;MINT;1;0;1;0;prod;8;1;24 GOLD 24 COAL;400 COINS\n"
            + "Mint Mines;MINT;1;0;1;0;prod;8;1;24 GOLD 24 COAL;400 COINS\n"
            + "Mint Mines & Burner;MINT;1;0;1;0;prod;8;1;24 GOLD 24 COAL;400 COINS\n"
            + "Oast House;OAST_HOUSE;1;0;0;0;prod;8;24;1 LOGS;1 HOPS\n"
            + "Peddler;PEDDLER;1;1;1;1;other;8;24; ;\n"
            + "Pigstry;PIGSTRY;1;1;0;0;prod;10;24;1 TURNIPS;1 MEAT 1 LEATHER\n"
            + "Pillow maker Chickens;PILLOW_MAKER;1;0;1;0;prod;8;24;1 TEXTILE 1 FEATHERS;1 PILLOWS\n"
            + "Pillow maker Hunt;PILLOW_MAKER;0;1;1;1;prod;8;24;1 TEXTILE 1 FEATHERS;1 PILLOWS\n"
            + "Pillow maker Market;PILLOW_MAKER;1;1;1;1;prod;8;24;1 TEXTILE 1 FEATHERS;1 PILLOWS\n"
            + "Quarry;QUARRY;1;1;1;1;prod;16;12;;1 STONE\n"
            + "Sawyer;SAWMILL;1;1;1;1;prod;6;24;1 LOGS;1 PLANKS\n"
            + "Goats Sheperd;SHEPERD;0;1;0;1;prod;8;24; ;1 MILK\n"
            + "Sheeps Sheperd;SHEPERD;0;1;0;1;prod;8;24; ;1 WOOL\n"
            + "Shoemaker Pigs;SHOEMAKER;1;1;0;0;prod;10;24;1 LEATHER;1 BOOTS\n"
            + "Shoemaker Hunt;SHOEMAKER;0;1;1;1;prod;10;24;1 LEATHER;1 BOOTS\n"
            + "Shoemaker Market;SHOEMAKER;1;1;1;1;prod;10;24;1 LEATHER;1 BOOTS\n"
            + "Smelter Diggers;SMELTERY;1;1;1;1;prod;8;24;1 IRON_ORE 1 COAL;1 IRON\n"
            + "Smelter Mines;SMELTERY;1;1;1;1;prod;8;24;1 IRON_ORE 1 COAL;1 IRON\n"
            + "Smelter Mines & Burner;SMELTERY;1;1;1;1;prod;8;24;1 IRON_ORE 1 COAL;1 IRON\n"
            + "Smoker Pigs;SMOKEHOUSE;1;1;0;0;prod;8;24;1 MEAT 1 LOGS;1 WURST\n"
            + "Smoker Hunt;SMOKEHOUSE;0;1;1;1;prod;8;24;1 MEAT 1 LOGS;1 WURST\n"
            + "Smoker Market;SMOKEHOUSE;1;1;1;0;prod;8;24;1 MEAT 1 LOGS;1 WURST\n"
            + "Storage Yard;STORAGE_YARD;1;1;1;1;other;8;24; ;\n"
            + "Cotton Weaver;WEAVER;1;1;1;1;prod;8;24;1 COTTON;1 TEXTILE\n"
            + "Wool Weaver;WEAVER;1;1;1;1;prod;8;24;1 WOOL;1 TEXTILE\n"
            + "Well;WELL;1;1;1;0;prod;0;96; ;1 WATER\n"
            + "Winery;WINERY;0;0;1;0;prod;24;24; ;1 WINE\n"
            + "Woodburner;WOODBURNER;1;1;1;1;prod;12;24;2 LOGS;1 COAL\n"
            + "Woodcutter;WOODCUTTER;1;1;1;1;prod;4;24;1 TREES;2 LOGS\n";
    
    public static final String INPUT_FILE_NAME  = "tsvin.txt";
    public static final String OUTPUT_FILE_NAME = "tsvout.txt";
    
    public static void main(String[] args) throws IOException {
        
        List<String> testData = Arrays.asList(TEST_DATA.split("\\R"));
        
        Building.buildAll(testData);
        List<String> output = Building.allToString();
        
        for(String line : output) {
            System.out.println(line);
        }
        
        
        Path tsvinPath  = Paths.get(INPUT_FILE_NAME);
        Path tsvoutPath = Paths.get(OUTPUT_FILE_NAME);
        
        List<String> tsvData = Files.readAllLines(tsvinPath);
        Files.write(tsvoutPath, output);
        
    }
    
}
