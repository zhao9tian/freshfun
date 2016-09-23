package com.quxin.freshfun.model;

import java.util.List;


public class HomePagePOJO {
	
	private List<SpecialMall> specialMall;
	
	private List<GoodsTypePOJO> goodsType;
	
	private List<GoodsTypePOJO> selection;
	
	private List<SpecialTheme> specialTheme;
	
	private List<GoodsPOJO> goodsByLimit;


	public List<GoodsTypePOJO> getGoodsType() {
		return goodsType;
	}

	public List<SpecialMall> getSpecialMall() {
		return specialMall;
	}

	public void setSpecialMall(List<SpecialMall> specialMall) {
		this.specialMall = specialMall;
	}

	public void setGoodsType(List<GoodsTypePOJO> goodsType) {
		this.goodsType = goodsType;
	}


	public List<GoodsTypePOJO> getSelection() {
		return selection;
	}

	public void setSelection(List<GoodsTypePOJO> selection) {
		this.selection = selection;
	}


	public List<SpecialTheme> getSpecialTheme() {
		return specialTheme;
	}

	public void setSpecialTheme(List<SpecialTheme> specialTheme) {
		this.specialTheme = specialTheme;
	}

	public List<GoodsPOJO> getGoodsByLimit() {
		return goodsByLimit;
	}

	public void setGoodsByLimit(List<GoodsPOJO> goodsByLimit) {
		this.goodsByLimit = goodsByLimit;
	}

}
