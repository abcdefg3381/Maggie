/**
 * 
 */
package maggie.network.entity;

/**
 * @author LIU Xiaofan
 * 
 */
public interface Node {

	void addDegree(int i);

	public void addInDegree();

	public void addOutDegree();

	@Override
	public boolean equals(Object obj);

	int getDegree();

	<T> T getID();

	float getInStrength();

	String getName();

	float getOutStrength();

	float getStrength();

	void setInStrength(float f);

	void setOutStrength(float f);

	@Override
	public String toString();
}
