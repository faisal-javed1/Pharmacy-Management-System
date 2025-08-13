package main;

import controller.CalculatorController;
import model.CalculatorModel;
import view.CalculatorView;

public class MyCalcApp {
	public static void main(String[] args) {
		CalculatorModel m = new CalculatorModel();
		CalculatorView v = new CalculatorView();
		CalculatorController c = new CalculatorController(m, v);
		v.setVisible(true);
	}
}
