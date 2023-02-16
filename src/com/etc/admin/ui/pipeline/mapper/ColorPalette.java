package com.etc.admin.ui.pipeline.mapper;

/**
 * <p>
 * ColorPalette is used to color field sets and cell rows within a mapper form.</p>
 * 
 * @author Pete Morgan 12/3/2020
 */
public enum ColorPalette
{
	COLOR01("#d98880"),
	COLOR02("#f5b7b1"),
	COLOR03("#c39bd3"),
	COLOR04("#d2b4de"),
	COLOR05("#7fb3d5"),
	COLOR06("#aed6f1"),
	COLOR07("#76d7c4"),
	COLOR08("#a2d9ce"),
	COLOR09("#7dcea0"),
	COLOR10("#abebc6"),
	COLOR11("#f7dc6f"),
	COLOR12("#fad7a0"),
	COLOR13("#f0b27a"),
	COLOR14("#edbb99"),
	COLOR15("#6666ff"),
	COLOR16("#6699ff"),
	COLOR17("#66ccff"),
	COLOR18("#66ffff"),
	COLOR19("#9900ff"),
	COLOR20("#9933ff"),
	COLOR21("#9966ff"),
	COLOR22("#9999ff"),
	COLOR23("#99ffff"),
	COLOR24("#cc00ff"),
	COLOR25("#cc66ff"),
	COLOR26("#cc99ff"),
	COLOR27("#ccccff"),
	COLOR28("#ccffff"),
	COLOR29("#ff00ff"),
	COLOR30("#ff33ff"),
	COLOR31("#8000ff"),
	COLOR32("#add8e6"),
	COLOR33("#00ff00"),
	COLOR34("#98afc7"),
	COLOR35("#736aff"),
	COLOR36("#87afc7"),
	COLOR37("#95b9c7"),
	COLOR38("#6698ff"),
	COLOR39("#5cb3ff"),
	COLOR40("#3bb9ff"),
	COLOR41("#82cafa"),
	COLOR42("#5efb6e"),
	COLOR43("#6afb92"),
	COLOR44("#98ff98"),
	COLOR45("#c3fdb8"),
	COLOR46("#f3e5ab"),
	COLOR47("#ffe5b4"),
	COLOR48("#fbb917"),
	COLOR49("#deb887"),
	COLOR50("#ffcba4"),
	COLOR51("#c19a6b"),
	COLOR52("#c47451"),
	COLOR53("#c35817"),
	COLOR54("#f87217"),
	COLOR55("#ff8040"),
	COLOR56("#f88158"),
	COLOR57("#f75d59"),
	COLOR58("#e8adaa"),
	COLOR59("#edc9af"),
	COLOR60("#fbbbb9"),
	COLOR61("#fcdfff"),
	COLOR62("#f778a1"),
	COLOR63("#e3e4fa"),
	COLOR64("#893bff"),
	COLOR65("#b041ff"),
	COLOR66("#e6a9ec"),
	COLOR67("#e0b0ff"),
	COLOR68("#ebdde2"),
	COLOR69("#ff8080"),
	COLOR70("#c6ffb3"),
	COLOR71("#80b3ff"),
	
	AZURE("#f0ffff");

	private String color = null;

	private ColorPalette(String color) { this.color = color; }

	public String getColor() { return color; }
}