package VRP;

public class Node
{
    private int x;
    private int y;
    private int demand;
    
    Node()
    {
        this.x = 0;
        this.y = 0;
        this.demand = 0;
    }
    
    Node(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.demand = 0;
    }
    
    public double distance(Node n)
    {
        return Math.sqrt(Math.pow((n.getX() - this.getX()),2) + Math.pow((n.getY() - this.getY()),2));
    }
    
    public void print()
    {
        System.out.println("(" + this.x + " , " + this.y + ") - demand: " + this.demand);
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int get_demand() {
        return demand;
    }

    public void set_demand(int demand)
    {
        this.demand = demand;
    }
}
