package VRP;

import java.io.FileNotFoundException;
import java.util.LinkedList;

import org.jgap.*;
import org.jgap.impl.*;

public class Vrp
{
	static Configuration config;
	static VrpConfiguration vrp_config;
	static FitnessFunction fitness_function;
	static Genotype population;
	static long evolution_time = 0;
	
	public Vrp() throws FileNotFoundException, InvalidConfigurationException
	{
		// https://mathewjhall.wordpress.com/2013/02/18/jgap-default-initialisation/
		config = new DefaultConfiguration();
		config.setPreservFittestIndividual(true);
		config.setKeepPopulationSizeConstant(true);
		
		vrp_config = new VrpConfiguration("benchmarks/A-n45-k6-in.txt", Consts.NO_VEHICLES);
		
		vrp_config.print();
		
		fitness_function = new VrpFitnessFunction(vrp_config);
		config.setFitnessFunction(fitness_function);
		
		Gene[] sample_genes = new Gene[vrp_config.GRAPH_DIMENSION];
		
		for(int i=0; i < vrp_config.GRAPH_DIMENSION; i++)
			sample_genes[i] = new IntegerGene(config, 0, (vrp_config.VEHICLE_NUMBER - 1));
		
		IChromosome sample_chromosome = new Chromosome(config, sample_genes);
		config.setSampleChromosome(sample_chromosome);
		config.setPopulationSize(100);
		
		// trzeba ustalic operatory
//		config.getNaturalSelectors(false).clear();
//        config.addGeneticOperator(new CrossoverOperator());
//        config.addGeneticOperator(new SwappingMutationOperator());

        population = Genotype.randomInitialGenotype(config);
	}

	
	public long go() throws RuntimeException
	{
		long start_time = System.currentTimeMillis();
		
		for (int i = 0; i < Consts.MAX_EVOLUTIONS; i++)
		{
        	if(i%50 == 0)
        	{
        		System.out.print(".");
        		System.out.println(get_best_solution_fitness());
        	}
        	if(i%5000 == 0)
        		System.out.println("");
        	if (!unique_chromosomes(population.getPopulation()))
        		throw new RuntimeException("Populacja niepoprawna w " + i + " generacji");

        	population.evolve();
        }
		
		long end_time = System.currentTimeMillis();
		
		evolution_time = end_time - start_time;
		
		return evolution_time;
	}

	public boolean unique_chromosomes(Population pop)
	{
		for(int i=0; i < pop.size() - 1; i++)
		{
			IChromosome c = pop.getChromosome(i);
			for(int j=i+1; j < pop.size(); ++j)
			{
				IChromosome c2 = pop.getChromosome(j);
				if (c == c2)
					return false;
			}
		}
		
		return true;
	}
	
	public double get_best_solution_fitness()
	{
		IChromosome bestSolutionSoFar = population.getFittestChromosome();
        return bestSolutionSoFar.getFitnessValue();
	}
	
	public void print_result_graph()
	{
		IChromosome bestSolutionSoFar = population.getFittestChromosome();
        bestSolutionSoFar.setFitnessValueDirectly(-1);
        
        System.out.println("Rezultat: ");
        
        for (int i = 0; i < vrp_config.GRAPH_DIMENSION; i++)
           System.out.println(i +". " + VrpFitnessFunction.get_number_at_gene(bestSolutionSoFar, i) );  
        
        Double  distance = 0.0;
        Double  distancep= 0.0;
        LinkedList<Integer> routes;
        for(int i = 0; i < vrp_config.VEHICLE_NUMBER; ++i)
        {
            distancep = VrpFitnessFunction.get_distance(i, bestSolutionSoFar, vrp_config);
            routes = VrpFitnessFunction.get_positions(i, bestSolutionSoFar, vrp_config);
            System.out.print("Route #" + i + " :");
            while(!routes.isEmpty())
            {
                int pos = ((Integer) routes.pop()).intValue();
                System.out.print(pos + ", ");
            }
            System.out.println();
            System.out.println("\t Dlugosc drogi: "+distancep);
            distance += distancep;
        }
        System.out.println("Calkowita dlugosc drog: " + distance);
        System.out.println();
	}
}
