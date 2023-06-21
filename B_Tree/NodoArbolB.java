package B_Tree;

public class NodoArbolB {
	int n; //numero de claves almacenadas en el nodo
	boolean leaf; //si el nodo es hoja (nodo hoja=true; nodo interno=false)
	int key[];	//almacena las claves en el nodo
	NodoArbolB child[];	//arreglo con referencia a los hijos
	
	//CONSTRUCTORES
	public NodoArbolB (int t) {
		this.n = 0;
		this.leaf = true;
		this.key = new int[(2 * t) - 1];
		this.child = new NodoArbolB[2 * t];
	}
	
	public void imprimir() {
		System.out.print("[");
		for (int i = 0; i < this.n; i++) {
			if (i < this.n-1) {
				System.out.print(this.key[i] + " | ");
			} else {
				System.out.print(this.key[i]);
			}
		}
		System.out.print("]");
	}
	
	public int find(int k) {
		for (int i = 0; i < n; i++) {
			if (this.key[i] == k) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < this.key.length; i++) {
			s += this.key[i] + " ";
		}
		s += "\n";
		return s;
	}
}
