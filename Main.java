package hearthlandsoptimizer;

public class Main {
    public static final String TEST_DATA = "Apothecary;APOTHECARY;1;1;1;1;prod;8;24;;\n"
            + "Aviary;AVIARY;1;1;1;0;prod;8;24;;\n"
            + "Bakery;BAKERY;1;0;1;0;prod;10;24;;\n"
            + "Beekeeper;BEEKEEPER;0;0;0;1;prod;12;24;;\n"
            + "Beer Brewery;BEER_BREWERY;1;0;0;0;prod;12;24;;\n"
            + "Carpenter;CARPENTER;1;1;1;1;prod;10;24;;\n"
            + "Cider Maker;CIDER_BREWERY;0;1;0;0;prod;18;24;;\n"
            + "Clothier;CLOTHIER;1;1;1;1;prod;6;24;;\n"
            + "Coal Digger;COAL_DIGGER;1;1;1;1;prod;8;24;;\n"
            + "Dairy;DIARY;0;1;0;0;prod;8;24;;\n"
            + "Fishing Quay;FISHING_QUAY;0;0;0;1;prod;12;24;;\n"
            + "Flowers Forestry;FORESTER;1;1;1;1;prod;4;48;;\n"
            + "Mushroom Forestry;FORESTER;1;1;1;1;prod;4;48;;\n"
            + "Tree Forestry;FORESTER;1;1;1;1;prod;4;48;;\n"
            + "Gold Digger;GOLD_DIGGER;1;0;1;0;prod;12;24;;\n"
            + "Apple Grower;GROWER;1;1;1;0;prod;8;24;;\n"
            + "Cotton Grower;GROWER;1;1;1;0;prod;8;24;;\n"
            + "Grain Grower;GROWER;1;1;1;0;prod;8;24;;\n"
            + "Turnip Grower;GROWER;1;1;1;0;prod;8;24;;\n"
            + "Herbalist;HERBALIST;1;1;1;1;prod;12;24;;\n"
            + "Boar Hunter;HUNTER;0;1;1;1;prod;12;24;;\n"
            + "Rabbit Hunter;HUNTER;0;1;1;1;prod;12;24;;\n"
            + "Turkey Hunter;HUNTER;0;1;1;1;prod;12;24;;\n"
            + "Iron Digger;IRON_DIGGER;1;1;1;1;prod;12;24;;\n"
            + "Marketplace;MARKETPLACE;1;1;1;1;;12;24;;\n"
            + "Masonry;MASONRY;1;1;1;1;prod;8;24;;\n"
            + "Mead Brewer;MEAD_BREWERY;0;0;0;1;prod;8;24;;\n"
            + "Mill;MILL;1;0;1;0;prod;4;24;;\n"
            + "Mint;MINT;1;0;1;0;prod;8;24;;\n"
            + "Oast House;OAST_HOUSE;1;0;0;0;prod;8;24;;\n"
            + "Peddler;PEDDLER;1;1;1;1;;8;24;;\n"
            + "Pigstry;PIGSTRY;1;1;0;0;prod;10;24;;\n"
            + "Pillow maker;PILLOW_MAKER;1;1;1;1;prod;8;24;;\n"
            + "Sawyer;SAWMILL;1;1;1;1;prod;6;24;;\n"
            + "Goats Sheperd;SHEPERD;0;1;0;1;prod;8;24;;\n"
            + "Sheeps Sheperd;SHEPERD;0;1;0;1;prod;8;24;;\n"
            + "Shoemaker;SHOEMAKER;1;1;1;1;prod;10;24;;\n"
            + "Smelter;SMELTERY;1;1;1;1;prod;8;24;;\n"
            + "Smoker;SMOKEHOUSE;1;1;1;0;prod;8;24;;\n"
            + "Storage Yard;STORAGE_YARD;1;1;1;1;;8;24;;\n"
            + "Cotton Weaver;WEAVER;1;1;1;1;prod;8;24;;\n"
            + "Wool Weaver;WEAVER;1;1;1;1;prod;8;24;;\n"
            + "Winery;WINERY;0;0;1;0;prod;24;24;;\n"
            + "Woodburner;WOODBURNER;1;1;1;1;prod;12;24;;\n"
            + "Woodcutter;WOODCUTTER;1;1;1;1;prod;4;24;;\n";
    
    public static void main(String[] args) {
        Building.buildAll(TEST_DATA);
        
    }
    
}
