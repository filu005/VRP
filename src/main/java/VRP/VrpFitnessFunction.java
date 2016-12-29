package VRP;

import java.util.LinkedList;
import org.jgap.*;

public class VrpFitnessFunction extends FitnessFunction
{
	private static VrpConfiguration vrp_config;
	
    public VrpFitnessFunction(VrpConfiguration config)
    {
        System.out.println("Inicjalizacja funkcji dopasowania");
        this.vrp_config = config;
    }
    
	@Override
	protected double evaluate(IChromosome chromosome)
	{
		double fitness = 0.0;
		
		// dla kazdego pojazdu
		for(int i = 0; i < this.vrp_config.VEHICLE_NUMBER; ++i)
		{
//			fitness += this.is_present(i, chromosome);
			
			fitness += this.get_distance(i, chromosome, this.vrp_config)*10;
            
            fitness += this.get_capacity(i, this.vrp_config.VEHICLE_CAPACITY, chromosome);
        }
		
		if (fitness < 0.0)
			return 0.0;
		
		// ??
	    fitness = 100000.0 - fitness;
		return Math.max(1.0, fitness);
	}
	

    public double is_present(int vehicleNumber, IChromosome chromosome)
    {
        LinkedList<Integer> positions = get_positions(vehicleNumber, chromosome, this.vrp_config);
        if(positions.size() > 0)
            return 0.0;
        
        return 100.0;
    }
    
    public static double get_distance(int vehicleNumber, IChromosome chromosome, VrpConfiguration vrpconf)
    {
        double totalDistance = 0.0;
        LinkedList<Integer> positions = get_positions(vehicleNumber, chromosome, vrpconf);
        Node deposit = vrpconf.nodes[0];
        Node final_destination = deposit;
        
        while(!positions.isEmpty())
        {
            int pos = ((Integer) positions.pop()).intValue();
            Node destination  = vrpconf.nodes[pos];
            totalDistance += final_destination.distance(destination);
            final_destination = destination;
        }
        
        totalDistance += final_destination.distance(deposit);

        return totalDistance;
    }
    
    public double get_capacity(int vehicleNumber, int vehicleCapacity, IChromosome chromosome)
    {
        double total_demand = 0.0;
        LinkedList<Integer> positions = get_positions(vehicleNumber, chromosome, this.vrp_config);
        
        while(!positions.isEmpty())
        {
            int pos = ((Integer) positions.pop()).intValue();
            Node destination  = this.vrp_config.nodes[pos];
            total_demand += destination.get_demand();
        }
        
        // duza kara za przekroczenie pojemnosci 'ladowni'
        if(total_demand > vehicleCapacity)
            return (total_demand - vehicleCapacity)*10;

        // mala kara za niezapelnienie ladowni do konca
        return (vehicleCapacity - total_demand)*2;
    }
    
    public static LinkedList<Integer> get_positions(int vehicleNumber, IChromosome chromosome, VrpConfiguration vrpconf)
    {
        LinkedList<Integer> p = new LinkedList<Integer>();

        // Pomijam pierwszy element (stacje poczatkowa)
        for(int i = 1; i < vrpconf.GRAPH_DIMENSION; ++i)
        {
            int cromosome_value = ((Integer) chromosome.getGene(i).getAllele()).intValue();
            
            if(cromosome_value == vehicleNumber)
               p.add(i);
        }
        
        return p;
    }
    
    public static double get_number_at_gene(IChromosome a_potentialSolution, int a_position)
    {
        Integer value = ((Integer) a_potentialSolution.getGene(a_position).getAllele()).intValue();
        return value.intValue();
    }

}
