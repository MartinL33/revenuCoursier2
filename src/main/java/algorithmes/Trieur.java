package algorithmes;

import java.util.ArrayList;

public interface Trieur<T extends ComparableWithArg<T>> {   
    void setArray(ArrayList<T> array);
    void setModeTri(Object modeTri);
    void tri();
}
