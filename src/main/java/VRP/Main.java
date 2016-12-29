package VRP;

import java.io.FileNotFoundException;

import org.jgap.InvalidConfigurationException;

public class Main
{
	public static void main(String[] args)
	{
		try
		{
			Vrp vrp = new Vrp();
			long run_time = vrp.go();
			
			double best_fitness = vrp.get_best_solution_fitness();
			
			vrp.print_result_graph();
			
			System.out.println("\nrun time: " + run_time + " ms\nbest fitness: " + best_fitness);
		}
		catch (FileNotFoundException | InvalidConfigurationException e)
		{
			System.out.println(e);
			e.printStackTrace();
		}

	}
}