package com.btb.nixorstudentapplication.Past_papers;

import com.google.firebase.firestore.Query;


public class queryVariable {
    private Query query = null;
    private ChangeListener listener;

    public Query isBoo() {
        return query;
    }

    public void setBoo(Query query) {
        this.query = query;
        if (listener != null) listener.onChange();







    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }
}