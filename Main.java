package hearthlandsoptimizer;

public class Main {
    public static final String TEST_DATA = "Apothecary;APOTHECARY;1;1;1;1;other;8;24; ;\n"
            + "Apple Grower;GROWER;1;1;1;0;prod;8;24; ;1 APPLES\n"
            + "Aviary;AVIARY;1;0;1;0;prod;8;24; ;1 EGGS 1 FEATHERS\n"
            + "Bakery;BAKERY;1;0;1;0;prod;10;24;1 FLOUR 1 LOGS 1 WATER;1 BREAD\n"
            + "Beekeeper;BEEKEEPER;0;0;0;1;prod;12;24;2 FLOWERS;1 HONEY\n"
            + "Beer Brewery;BEER_BREWERY;1;0;0;0;prod;12;24;1 HOPS 1 GRAIN 1 LOGS;1 BEER\n"
            + "Boar Hunter;HUNTER;0;1;1;1;prod;12;12; ;1 MEAT 1 LEATHER\n"
            + "Carpenter;CARPENTER;1;1;1;1;prod;10;24;1 PLANKS;1 FURNITURE\n"
            + "Cider Maker;CIDER_BREWERY;0;1;0;0;prod;18;24;1 APPLES 1 LOGS 1 WATER;1 CIDER\n"
            + "Clothier;CLOTHIER;1;1;1;1;prod;6;24;1 TEXTILE;1 CLOTHES\n"
            + "Coal Digger;COAL_DIGGER;1;1;1;1;prod;8;12; ;1 COAL\n"
            + "Cotton Grower;GROWER;1;1;1;0;prod;8;24; ;1 COTTON\n"
            + "Cotton Weaver;WEAVER;1;1;1;1;prod;8;24;1 COTTON;1 TEXTILE\n"
            + "Dairy;DIARY;0;1;0;0;prod;8;24;1 MILK;1 CHEESE\n"
            + "Fishing Quay;FISHING_QUAY;0;0;0;1;prod;12;24; ;1 FISH\n"
            + "Flowers Forestry;FORESTER;1;1;1;1;prod;4;48; ;1 FLOWERS\n"
            + "Goats Sheperd;SHEPERD;0;1;0;1;prod;8;24; ;1 MILK\n"
            + "Gold Digger;GOLD_DIGGER;1;0;1;0;prod;12;12; ;1 GOLD\n"
            + "Grain Grower;GROWER;1;1;1;0;prod;8;24; ;1 GRAIN\n"
            + "Herbalist;HERBALIST;1;1;1;1;prod;12;24;1 MUSHROOMS 1 LOGS;1 MEDICINE\n"
            + "Iron Digger;IRON_DIGGER;1;1;1;1;prod;12;12; ;1 IRON_ORE\n"
            + "Marketplace;MARKETPLACE;1;1;1;1;other;12;24; ;\n"
            + "Masonry;MASONRY;1;1;1;1;prod;8;24; ;\n"
            + "Mead Brewer;MEAD_BREWERY;0;0;0;1;prod;8;24;1 HONEY;1 MEAD\n"
            + "Mill;MILL;1;0;1;0;prod;4;24;1 GRAIN;1 FLOUR\n"
            + "Mint;MINT;1;0;1;0;prod;8;1;24 GOLD 24 COAL;400 COINS\n"
            + "Mushroom Forestry;FORESTER;1;1;1;1;prod;4;48; ;1 MUSHROOMS\n"
            + "Oast House;OAST_HOUSE;1;0;0;0;prod;8;24;1 LOGS;1 HOPS\n"
            + "Peddler;PEDDLER;1;1;1;1;other;8;24; ;\n"
            + "Pigstry;PIGSTRY;1;1;0;0;prod;10;24;1 TURNIPS;1 MEAT 1 LEATHER\n"
            + "Pillow maker;PILLOW_MAKER;1;1;1;1;prod;8;24;1 TEXTILE 1 FEATHERS;1 PILLOWS\n"
            + "Rabbit Hunter;HUNTER;0;1;1;1;prod;12;12; ;1 MEAT 1 WOOL\n"
            + "Sawyer;SAWMILL;1;1;1;1;prod;6;24;1 LOGS;1 PLANKS\n"
            + "Sheeps Sheperd;SHEPERD;0;1;0;1;prod;8;24; ;1 WOOL\n"
            + "Shoemaker;SHOEMAKER;1;1;1;1;prod;10;24;1 LEATHER;1 BOOTS\n"
            + "Smelter;SMELTERY;1;1;1;1;prod;8;24;1 IRON_ORE 1 COAL;1 IRON\n"
            + "Smoker;SMOKEHOUSE;1;1;1;0;prod;8;24;1 MEAT 1 LOGS;1 WURST\n"
            + "Storage Yard;STORAGE_YARD;1;1;1;1;other;8;24; ;\n"
            + "Tree Forestry;FORESTER;1;1;1;1;prod;4;48; ;1 TREES\n"
            + "Turkey Hunter;HUNTER;0;1;1;1;prod;12;12; ;1 MEAT 1 FEATHERS\n"
            + "Turnip Grower;GROWER;1;1;1;0;prod;8;24; ;1 TURNIPS\n"
            + "Well;WELL;1;1;1;0;prod;0;96; ;1 WATER\n"
            + "Winery;WINERY;0;0;1;0;prod;24;24; ;1 WINE\n"
            + "Woodburner;WOODBURNER;1;1;1;1;prod;12;24;2 LOGS;1 COAL\n"
            + "Woodcutter;WOODCUTTER;1;1;1;1;prod;4;24;1 TREES;2 LOGS\n"
            + "Wool Weaver;WEAVER;1;1;1;1;prod;8;24;1 WOOL;1 TEXTILE\n";
    
    public static void main(String[] args) {
        Building.buildAll(TEST_DATA, false, true, false, false);
        System.out.println(Building.allToString());
        
    }
    
}
