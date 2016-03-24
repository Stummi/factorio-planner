package org.stummi.factorio;

import org.stummi.factorio.data.Item;
import org.stummi.factorio.data.Receipe;

public class Main {
	public static void main(String[] args) {
		Item ironGearWheel = new Item("Iron Gear Wheel");
		Item ironPlate = new Item("Iron Plate");
		Item copperPlate = new Item("Copper Plate");
		Item copperCable = new Item("Copper Cable");
		Item electronicCircuit = new Item("Electronic Circuit");
		Item inserter = new Item("Inserter");
		Item transportBelt = new Item("Transport Belt");
		Item fastInserter = new Item("Fast Inserter");
		Item smartInserter = new Item("Smart Inserter");
		Item advancedCircuit = new Item("Advanced Circuit");
		Item battery = new Item("Battery");
		Item steelPlate = new Item("Steel Plate");
		Item plasticBar = new Item("Plastic Bar");
		Item sulfuricAcid = new Item("Sulfuric Acid");
		Item science1 = new Item("Science Pack 1");
		Item science2 = new Item("Science Pack 2");
		Item science3 = new Item("Science Pack 3");
		Item speedModule = new Item("Speed Module");
		Item speedModule2 = new Item("Speed Module 2");
		Item speedModule3 = new Item("Speed Module 3");
		Item processingUnit = new Item("Processing Unit");
		Item alienArtifact = new Item("Alien Artifact");

		Receipe copperCableReceipe = Receipe.singleProduct(30, copperCable.twice(), copperPlate.once());
		Receipe ironGearWheelReceipe = Receipe.singleProduct(30, ironGearWheel.once(), ironPlate.twice());
		Receipe science1Receipe = Receipe.singleProduct(300, science1.once(), copperPlate.once(), ironGearWheel.once());
		Receipe science2Receipe = Receipe.singleProduct(360, science2.once(), inserter.once(), transportBelt.once());
		Receipe science3Receipe = Receipe.singleProduct(720, science3.once(), smartInserter.once(), battery.once(), advancedCircuit.once(),
				steelPlate.once());
		Receipe electronicCircuitReceipe = Receipe.singleProduct(30, electronicCircuit.once(), ironPlate.once(), copperCable.amount(3));
		Receipe inserterReceipe = Receipe.singleProduct(30, inserter.once(), electronicCircuit.once(), ironGearWheel.once(), ironPlate.once());
		Receipe transportBeltReceipe = Receipe.singleProduct(30, transportBelt.twice(), ironPlate.once(), ironGearWheel.once());
		Receipe fastInserterReceipe = Receipe.singleProduct(30, fastInserter.once(), inserter.once(), ironPlate.twice(), electronicCircuit.twice());
		Receipe smartInserterReceipe = Receipe.singleProduct(30, smartInserter.once(), fastInserter.once(), electronicCircuit.amount(4));
		Receipe steelPlateReceipe = Receipe.singleProduct(1050, steelPlate.once(), ironPlate.amount(5));
		Receipe advancedCircuitReceipe = Receipe.singleProduct(8 * 60, advancedCircuit.once(), electronicCircuit.twice(), plasticBar.twice(),
				copperCable.amount(4));
		Receipe batteryReceipe = Receipe.singleProduct(300, battery.once(), ironPlate.once(), copperCable.once(), sulfuricAcid.twice());
		Receipe speedModuleReceipe = Receipe.singleProduct(900, speedModule.once(), advancedCircuit.amount(5), electronicCircuit.amount(5));
		Receipe speedModule2Receipe = Receipe.singleProduct(1800, speedModule2.once(), advancedCircuit.amount(5), processingUnit.amount(5),
				speedModule.amount(4));
		Receipe processingUnitReceipe = Receipe.singleProduct(900, processingUnit.once(), electronicCircuit.amount(20), advancedCircuit.amount(2),
				sulfuricAcid.amount(.5));
		Receipe speedModule3Receipe = Receipe.singleProduct(3600, speedModule3.once(), speedModule2.amount(4), advancedCircuit.amount(5),
				processingUnit.amount(5), alienArtifact.once());

		// Research Labs example

		Plan plan = new Plan();
		plan.addReceipe(science1Receipe, 4);
		plan.addReceipe(science2Receipe, 4);
		plan.addReceipe(science3Receipe, 8);
		plan.addReceipe(inserterReceipe, 1);
		plan.report().print(System.out);
	}
}
