package porprezhas;

import java.util.ArrayList;
import java.util.Collection;

public class CircularArrayList<E> extends ArrayList<E> {

    /**
     * Get element from list
     *
     * @param index Index of Object in the list
     * @return  The Object in the index position of list
     */
    @Override
    public E get(int index) {

        // from tail to head
        if (index > size()) {
            index = index % size();

        // from head to tail
        } else if(index < 0) {
            index += size();
        }

        return super.get(index);
    }

}
