package com.jeremelau.rxbus;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Author: Created by jereme on 2018/7/5.
 * E-mail: liuqx@guoguang.com.cn
 */

public class RxBus {
    private static RxBus rxBus;
    private final Subject bus = PublishSubject.create().toSerialized();
    private final Map<Object, List<Subject>> subjectMapper = new HashMap();

    public RxBus() {
    }

    public static RxBus getInstance() {
        if (rxBus == null) {
            rxBus = new RxBus();
        }

        return rxBus;
    }

    public Observable<Object> toObserverable() {
        return this.bus;
    }

    public void send(Object o) {
        this.bus.onNext(o);
    }

    public <T> Observable<T> register(@NonNull Object tag, @NonNull Class<T> c) {
        Subject subject = PublishSubject.create().toSerialized();
        //Map var4 = this.subjectMapper;
        synchronized(this.subjectMapper) {
            List<Subject> subjectList = this.subjectMapper.get(tag);
            if (null == subjectList) {
                subjectList = new CopyOnWriteArrayList();
                this.subjectMapper.put(tag, subjectList);
                this.send(new RxBus.RxBusEvent(tag, 0));
            }

            subjectList.add(subject);
            this.send(new RxBus.RxBusEvent(tag, 1, subject));
            return subject;
        }
    }

    public void unregister(@NonNull Object tag, @NonNull Observable observable) {
        //Map var3 = this.subjectMapper;
        synchronized(this.subjectMapper) {
            List<Subject> subjectList = this.subjectMapper.get(tag);
            if (null != subjectList) {
                subjectList.remove(observable);
                this.send(new RxBus.RxBusEvent(tag, 2, observable));
                if (subjectList.isEmpty()) {
                    this.subjectMapper.remove(tag);
                    this.send(new RxBus.RxBusEvent(tag, 3));
                }
            }

        }
    }

    public <T> void post(@NonNull Object tag, @NonNull T content) {
        List<Subject> subjectList = this.subjectMapper.get(tag);
        if (null != subjectList) {
            Iterator var4 = subjectList.iterator();

            while(var4.hasNext()) {
                Subject subject = (Subject)var4.next();
                subject.onNext(content);
            }
        }

    }

    public static class RxBusEvent {
        public static final int CREATE = 0;
        public static final int ADD = 1;
        public static final int REMOVE = 2;
        public static final int DESTROY = 3;
        private Object tag;
        private int type;
        private Observable observable;

        private RxBusEvent(Object tag, int type) {
            this(tag, type, null);
        }

        private RxBusEvent(Object tag, int type, Observable observable) {
            this.tag = tag;
            this.type = type;
            this.observable = observable;
        }

        public Object getTag() {
            return this.tag;
        }

        public int getType() {
            return this.type;
        }

        public Observable getObservable() {
            return this.observable;
        }
    }
}
