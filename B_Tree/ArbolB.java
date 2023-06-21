package B_Tree;

import java.util.ArrayList;

public class ArbolB {
	NodoArbolB root;
	int t;
	
	//CONSTRUCTOR
	public ArbolB(int t) {
		this.t = t;
		this.root = new NodoArbolB(t);
	}
	
	//Busca el valor ingresado y muestra el contenido del nodo que contiene el valor
	public void buscarNodoPorClave(int num) {
		NodoArbolB temp = search(this.root, num);
		
		if (temp == null) {
			System.out.println("No se ha encontrado un nodo con el valor ingresado");
		} else {
			System.out.print(temp);
		}
	}
	
	//SEARCH
	private NodoArbolB search(NodoArbolB actual, int key) {
		int i = 0; //Se empieza a buscar siempre en la primera posicion
		
		//Incrementa el indice mientras el valor de la clave del nodo sea mayor
		while (i < actual.n && key > actual.key[i]) {
			i++;
		}
		
		//Si la clave es igual, entonces retornamos el nodo
		if (i < actual.n && key == actual.key[i]) {
			return actual;
		}
		
		//Si llegamos hasta aqui, entonces hay que buscar los hijos
		//Se revisa primero si tiene hijos
		if (actual.leaf) {
			return null;
		} else {
			//Si tiene hijos hace una llamada recursiva
			return search(actual.child[i], key);
		}
	}
	
	public void insertar(int key) {
		NodoArbolB r = this.root;
		
		//Si el nodo esta lleno lo debe separar antes de insertar
		if (r.n == ((2*t) - 1)) {
			NodoArbolB s = new NodoArbolB(t);
			this.root = s;
			s.leaf = false;
			s.n = 0;
			s.child[0] = r;
			split(s, 0, r);
			nonFullInsert(s, key);
		} else {
			nonFullInsert(r, key);
		}
	}
	
	//Caso cuando la raiz se divide
	// x = 		 ||||||
	//			/
	//		|10|20|30|40|50|
	// i = 0
	// Y = |10|20|30|40|50|
	private void split(NodoArbolB x, int i, NodoArbolB y) {
		//Nodo temporal que sera el hijo i + 1 de x
		NodoArbolB z = new NodoArbolB(this.t);
		z.leaf = y.leaf;
		z.n = (t-1);
		
		//Copia las ultimas (t - 1) claves del nodo y al inicio del nodo z
		// z = |40|50||||
		for (int j = 0; j < (this.t- 1); j++) {
			z.key[j] = y.key[(j + this.t)];
		}
		
		//Si no es hoja hay que reasignar los nodos hijos
		if (!y.leaf) {
			for (int k = 0; k < this.t; k++) {
				z.child[k] = y.child[(k + this.t)];
			}
		}
		
		//Nuevo tamaño de y
		// 						x = 	||||||
		y.n = (this.t - 1);		// / \
		//			     |10|20||||   
		//Mueve los hijos de x para darle espacio a z
		for (int j = x.n; j > i; j--) {
			x.child[(j + 1)] = x.child[j];
		}
		//Reasigna el hijo (i+1) de x
		// 						x = 	||||||
		x.child[i + 1] = z;	   // / \
		//			     |10|20|||| |40|50|||| 
		//Mueve las claves de x
		for (int j = x.n; j > i; j--) {
			x.key[(j + 1)] = x.key[j];
		}
		//Agrega la clave situada en la mediana
		//					  	x = 	|30|||||
		x.key[i] = y.key[(t-1)];	//  / \
		x.n++;				// |10|20|||| |40|50||||
	}
	
	private void nonFullInsert(NodoArbolB x, int key) {
		//Si es una hoja
		if (x.leaf) {
			int i = x.n; //cantidad de valores del nodo
			//busca la posicion i donde asignar el valor
			while (i >= 1 && key < x.key[i - 1]) {
				x.key[i] = x.key[i - 1];//Desplaza los valores mayores a key
				i--;
			}
			x.key[i] = key;//asigna el valor al nodo
			x.n++; //aumenta la cantidad de elementos del nodo
		} else {
			int j = 0;
			//Busca la posicion del hijo
			while (j < x.n && key > x.key[j]) {
				j++;
			}
			//Si el nodo hijo esta lleno lo separa
			if (x.child[j].n == (2 * t - 1)) {
				split(x, j, x.child[j]);
					if (key > x.key[j]) {
						j++;
					}
			}
			nonFullInsert(x.child[j], key);
		}
	}
	
	public void showBTree() {
		print(root);
	}
	
	//Print en preorder
	private void print(NodoArbolB n) {
		n.imprimir();
		//Si no es hoja
		if (!n.leaf) {
			//recorre los nodos para saber si tiene hijos
			for (int j = 0; j <= n.n; j++) {
				if (n.child[j] != null) {
					System.out.println();
					print(n.child[j]);
				}
			}
		}
	}
	
	//EJERCICIO 1
	public void eliminarClave(int key) {
	    if (root.n == 0) {
	        System.out.println("El árbol está vacío. No se puede eliminar la clave.");
	        return;
	    }
	    
	    eliminar(root, key);
	    
	    // Si la raíz quedó vacía después de la eliminación, se actualiza la raíz del árbol.
	    if (root.n == 0 && !root.leaf) {
	        root = root.child[0];
	    }
	}

	private void eliminar(NodoArbolB nodo, int key) {
	    int i = 0;
	    while (i < nodo.n && key > nodo.key[i]) {
	        i++;
	    }
	    
	    if (i < nodo.n && key == nodo.key[i]) {
	        if (nodo.leaf) {
	            // Caso 1: El nodo es una hoja
	            desplazarClavesIzquierda(nodo, i);
	            nodo.n--;
	        } else {
	            // Caso 2: El nodo es un nodo interno
	            NodoArbolB hijoIzq = nodo.child[i];
	            NodoArbolB hijoDer = nodo.child[i + 1];
	            
	            if (hijoIzq.n >= t) {
	                // Caso 2a: El hijo izquierdo tiene al menos t claves
	                int predecessor = obtenerPredecesor(hijoIzq);
	                nodo.key[i] = predecessor;
	                eliminar(hijoIzq, predecessor);
	            } else if (hijoDer.n >= t) {
	                // Caso 2b: El hijo derecho tiene al menos t claves
	                int successor = obtenerSucesor(hijoDer);
	                nodo.key[i] = successor;
	                eliminar(hijoDer, successor);
	            } else {
	                // Caso 2c: Ambos hijos tienen t - 1 claves
	                fusionarNodos(nodo, i, hijoIzq, hijoDer);
	                eliminar(hijoIzq, key);
	            }
	        }
	    } else {
	        if (nodo.leaf) {
	            // La clave no existe en el árbol
	            System.out.println("La clave no existe en el árbol.");
	            return;
	        }
	        
	        NodoArbolB hijo = nodo.child[i];
	        if (hijo.n == t - 1) {
	            // Caso 3: El hijo tiene t - 1 claves, necesitamos asegurarnos de que tenga al menos t claves antes de continuar
	            if (i > 0 && nodo.child[i - 1].n >= t) {
	                // Caso 3a: El hermano izquierdo tiene al menos t claves
	                prestarDelHermanoIzquierdo(nodo, i, hijo, nodo.child[i - 1]);
	            } else if (i < nodo.n && nodo.child[i + 1].n >= t) {
	                // Caso 3a: El hermano derecho tiene al menos t claves
	                prestarDelHermanoDerecho(nodo, i, hijo, nodo.child[i + 1]);
	            } else {
	                // Caso 3b: Ambos hermanos tienen t - 1 claves
	                if (i > 0) {
	                    fusionarNodos(nodo, i - 1, nodo.child[i - 1], hijo);
	                    hijo = nodo.child[i - 1];
	                } else {
	                    fusionarNodos(nodo, i, hijo, nodo.child[i + 1]);
	                }
	            }
	        }
	        
	        eliminar(hijo, key);
	    }
	}

	// Método auxiliar para desplazar las claves a la izquierda a partir de la posición i
	private void desplazarClavesIzquierda(NodoArbolB nodo, int i) {
	    for (int j = i; j < nodo.n - 1; j++) {
	        nodo.key[j] = nodo.key[j + 1];
	    }
	}

	// Método auxiliar para obtener el predecesor de una clave en un nodo
	private int obtenerPredecesor(NodoArbolB nodo) {
	    if (nodo.leaf) {
	        return nodo.key[nodo.n - 1];
	    } else {
	        return obtenerPredecesor(nodo.child[nodo.n]);
	    }
	}

	// Método auxiliar para obtener el sucesor de una clave en un nodo
	private int obtenerSucesor(NodoArbolB nodo) {
	    if (nodo.leaf) {
	        return nodo.key[0];
	    } else {
	        return obtenerSucesor(nodo.child[0]);
	    }
	}

	// Método auxiliar para fusionar dos nodos y su clave correspondiente
	private void fusionarNodos(NodoArbolB padre, int idxClavePadre, NodoArbolB nodoIzq, NodoArbolB nodoDer) {
	    nodoIzq.key[t - 1] = padre.key[idxClavePadre];
	    
	    for (int i = 0; i < nodoDer.n; i++) {
	        nodoIzq.key[i + t] = nodoDer.key[i];
	    }
	    
	    if (!nodoIzq.leaf) {
	        for (int i = 0; i <= nodoDer.n; i++) {
	            nodoIzq.child[i + t] = nodoDer.child[i];
	        }
	    }
	    
	    for (int i = idxClavePadre; i < padre.n - 1; i++) {
	        padre.key[i] = padre.key[i + 1];
	    }
	    
	    for (int i = idxClavePadre + 1; i < padre.n; i++) {
	        padre.child[i] = padre.child[i + 1];
	    }
	    
	    nodoIzq.n += nodoDer.n + 1;
	    padre.n--;
	}

	// Método auxiliar para prestar una clave del hermano izquierdo
	private void prestarDelHermanoIzquierdo(NodoArbolB padre, int idxClavePadre, NodoArbolB hijo, NodoArbolB hermanoIzq) {
	    // Desplaza las claves y los hijos a la derecha en el hijo
	    for (int j = hijo.n - 1; j >= 0; j--) {
	        hijo.key[j + 1] = hijo.key[j];
	    }
	    if (!hijo.leaf) {
	        for (int j = hijo.n; j >= 0; j--) {
	            hijo.child[j + 1] = hijo.child[j];
	        }
	    }
	    
	    // Mueve la clave del padre al hijo
	    hijo.key[0] = padre.key[idxClavePadre - 1];
	    
	    // Mueve la última clave del hermano izquierdo al padre
	    padre.key[idxClavePadre - 1] = hermanoIzq.key[hermanoIzq.n - 1];
	    
	    if (!hermanoIzq.leaf) {
	        // Mueve el último hijo del hermano izquierdo al hijo
	        hijo.child[0] = hermanoIzq.child[hermanoIzq.n];
	    }
	    
	    hijo.n++;
	    hermanoIzq.n--;
	}

	// Método auxiliar para prestar una clave del hermano derecho
	private void prestarDelHermanoDerecho(NodoArbolB padre, int idxClavePadre, NodoArbolB hijo, NodoArbolB hermanoDer) {
	    // Mueve la clave del padre al hijo
	    hijo.key[hijo.n] = padre.key[idxClavePadre];
	    
	    // Mueve la primera clave del hermano derecho al padre
	    padre.key[idxClavePadre] = hermanoDer.key[0];
	    
	    // Mueve el primer hijo del hermano derecho al hijo
	    if (!hermanoDer.leaf) {
	        hijo.child[hijo.n + 1] = hermanoDer.child[0];
	    }
	    
	    // Desplaza las claves y los hijos a la izquierda en el hermano derecho
	    for (int j = 1; j < hermanoDer.n; j++) {
	        hermanoDer.key[j - 1] = hermanoDer.key[j];
	    }
	    if (!hermanoDer.leaf) {
	        for (int j = 1; j <= hermanoDer.n; j++) {
	            hermanoDer.child[j - 1] = hermanoDer.child[j];
	        }
	    }
	    
	    hijo.n++;
	    hermanoDer.n--;
	}
	
	//EJERCICIO 2
	// Método para obtener el camino recorrido hasta un nodo específico
	public ArrayList<NodoArbolB> obtenerCaminoRecorridoHastaNodo(int valorNodoDestino) {
        ArrayList<NodoArbolB> camino = new ArrayList<>();
        obtenerCaminoRecorridoHastaNodoRecursivo(this.root, valorNodoDestino, camino);
        return camino;
    }

    private boolean obtenerCaminoRecorridoHastaNodoRecursivo(NodoArbolB actual, int valorNodoDestino, ArrayList<NodoArbolB> camino) {
        camino.add(actual);
        for (int i = 0; i < actual.n; i++) {
            if (valorNodoDestino == actual.key[i]) {
                return true; // Se encontró el nodo destino, se termina la recursión
            } else if (valorNodoDestino < actual.key[i]) {
                return obtenerCaminoRecorridoHastaNodoRecursivo(actual.child[i], valorNodoDestino, camino);
            }
        }
        if (actual.leaf) {
            return false; // Llegó a una hoja, no se encontró el nodo destino
        }
        return obtenerCaminoRecorridoHastaNodoRecursivo(actual.child[actual.n], valorNodoDestino, camino);
    }
	
    //EJERCICIO 3
    // Método para buscar un valor y retornar las claves del nodo que lo contiene
    public ArrayList<Integer> buscarClavesNodo(int valor) {
        NodoArbolB nodo = buscarNodo(this.root, valor);
        if (nodo != null) {
            ArrayList<Integer> claves = new ArrayList<>();
            for (int i = 0; i < nodo.n; i++) {
                claves.add(nodo.key[i]);
            }
            return claves;
        }
        return null;
    }

    private NodoArbolB buscarNodo(NodoArbolB actual, int valor) {
        int i = 0;
        while (i < actual.n && valor > actual.key[i]) {
            i++;
        }
        if (i < actual.n && valor == actual.key[i]) {
            return actual; // Se encontró el nodo que contiene el valor
        }
        if (actual.leaf) {
            return null; // Llegó a una hoja y no se encontró el valor
        }
        return buscarNodo(actual.child[i], valor); // Búsqueda recursiva en el hijo correspondiente
    }
    
    //EJERCICIO 4
    // Método para obtener el valor máximo del árbol B
    public int obtenerValorMaximo() {
        if (root == null) {
            throw new IllegalStateException("El árbol está vacío");
        }
        NodoArbolB nodo = root;
        while (!nodo.leaf) {
            nodo = nodo.child[nodo.n];
        }
        return nodo.key[nodo.n - 1];
    }
    
    //EJERCICIO 5
    public NodoArbolB obtenerNodoMinimo() {
        NodoArbolB actual = this.root;
        
        while (!actual.leaf) {
            actual = actual.child[0];
        }
        
        // Formato de salida: [10 | 20]
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < actual.n; i++) {
            sb.append(actual.key[i]);
            if (i < actual.n - 1) {
                sb.append(" | ");
            }
        }
        sb.append("]");
        
        System.out.println(sb.toString());
        return actual;
    }
}
