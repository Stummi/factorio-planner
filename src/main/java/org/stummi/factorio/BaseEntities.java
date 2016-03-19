package org.stummi.factorio;

public interface BaseEntities {
	Product ironGearWheel = new Product("Iron Gear Wheel");
	Product ironPlate = new Product("Iron Plate");
	Product copperPlate = new Product("Copper Plate");
	Product copperCable = new Product("Copper Cable");
	Product electronicCircuit = new Product("Electronic Circuit");
	Product inserter = new Product("Inserter");
	Product transportBelt = new Product("Transport Belt");
	Product fastInserter = new Product("Fast Inserter");
	Product smartInserter = new Product("Smart Inserter");
	Product advancedCircuit = new Product("Advanced Circuit");
	Product battery = new Product("Battery");
	Product steelPlate = new Product("Steel Plate");
	Product plasticBar = new Product("Plastic Bar");
	Product sulfuricAcid = new Product("Sulfuric Acid");
	Product science1 = new Product("Science Pack 1");
	Product science2 = new Product("Science Pack 2");
	Product science3 = new Product("Science Pack 3");

	Receipe copperCableReceipe = Receipe.singleProduct(30, copperCable.twice(), copperPlate.once());
	Receipe ironGearWheelReceipe = Receipe.singleProduct(30, ironGearWheel.once(), ironPlate.twice());
	Receipe science1Receipe = Receipe.singleProduct(300, science1.once(), copperPlate.once(), ironGearWheel.once());
	Receipe science2Receipe = Receipe.singleProduct(360, science2.once(), inserter.once(), transportBelt.once());
	Receipe science3Receipe = Receipe.singleProduct(720, science3.once(), smartInserter.once(), battery.once(), advancedCircuit.once(), steelPlate.once());
	Receipe electronicCircuitReceipe = Receipe.singleProduct(30, electronicCircuit.once(), ironPlate.once(), copperCable.amount(3));
	Receipe inserterReceipe = Receipe.singleProduct(30, inserter.once(), electronicCircuit.once(), ironGearWheel.once(), ironPlate.once());
	Receipe transportBeltReceipe = Receipe.singleProduct(30, transportBelt.twice(), ironPlate.once(), ironGearWheel.once());
	Receipe fastInserterReceipe = Receipe.singleProduct(30, fastInserter.once(), inserter.once(), ironPlate.twice(), electronicCircuit.twice());
	Receipe smartInserterReceipe = Receipe.singleProduct(30, smartInserter.once(), fastInserter.once(), electronicCircuit.amount(4));
	Receipe steelPlateReceipe = Receipe.singleProduct(1050, steelPlate.once(), ironPlate.amount(5));
	Receipe advancedCircuitReceipe = Receipe.singleProduct(8 * 60, advancedCircuit.once(), electronicCircuit.twice(), plasticBar.twice(), copperCable.amount(4));
	Receipe batteryReceipe = Receipe.singleProduct(300, battery.once(), ironPlate.once(), copperCable.once(), sulfuricAcid.twice());
}
