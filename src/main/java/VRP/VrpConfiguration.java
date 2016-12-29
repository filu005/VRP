package VRP;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class VrpConfiguration
{
    public int GRAPH_DIMENSION = 1;
    public int VEHICLE_CAPACITY = 10;
    public int VEHICLE_NUMBER = 10;
    
	public Node nodes[];

	VrpConfiguration(String file, int vehicles) throws FileNotFoundException
	{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		String [] value;
		int i = 0;
		int x, y, d;
		boolean demand = false;
		
		try
		{
			VEHICLE_NUMBER = vehicles;
			while ((line = br.readLine()) != null)
			{
				if (line.contains("DIMENSION"))
				{
					value = line.split(":");
					GRAPH_DIMENSION = Integer.parseInt(value[1].trim());
				}
				else if (line.contains("CAPACITY"))
				{
					value = line.split(":");
					VEHICLE_CAPACITY = Integer.parseInt(value[1].trim());
					break;
				}
			}
			this.nodes = new Node[GRAPH_DIMENSION];
			line = br.readLine(); // this line is to jump the "NODE_COORD_SECTION" line
			while ((line = br.readLine()) != null)
			{
				if (line.contains("DEPOT_SECTION"))
					break;
				if (line.contains("DEMAND_SECTION"))
				{
					demand = true;
					i = 0;
					line = br.readLine();
				}
				if (!demand)
				{
					value = line.trim().split(" ");
					x = Integer.parseInt(value[1]);
					y = Integer.parseInt(value[2]);
					nodes[i++] = new Node(x, y);
				}
				else
				{
					value = line.trim().split(" ");
					d = Integer.parseInt(value[1]);
					nodes[i++].set_demand(d);
				}
			}
			System.out.println("Configuration created");
		}
		catch (IOException e)
		{
			System.err.println(e.toString());
		}
		finally
		{
			try
			{
				br.close();
			}
			catch (IOException ex)
			{
				System.err.println(ex.toString());
			}
		}
	}

	public void print()
	{
		System.out.println("GRAPH_DIMENSION: " + this.GRAPH_DIMENSION);
		System.out.println("VEHICLE_CAPACITY: " + this.VEHICLE_CAPACITY);
		System.out.println("VEHICLE_NUMBER: " + this.VEHICLE_NUMBER);
		
		for(int i=0; i<this.GRAPH_DIMENSION; ++i)
		{
			System.out.print("Node "+i+" ");
			this.nodes[i].print();
		}
	}
}
