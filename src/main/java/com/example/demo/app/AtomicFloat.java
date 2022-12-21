//package com.example.demo.app;
//
//import java.util.concurrent.atomic.AtomicInteger;
//
//import static java.lang.Float.floatToIntBits;
//import static java.lang.Float.intBitsToFloat;
//
//public class AtomicFloat extends Number {
//
//    private AtomicInteger bits;
//
//    public AtomicFloat() {
//        this(0f);
//    }
//
//    @Override
//    public int intValue() {
//        return 0;
//    }
//
//    @Override
//    public long longValue() {
//        return 0;
//    }
//
//    public AtomicFloat(float initialValue) {
//        bits = new AtomicInteger(floatToIntBits(initialValue));
//    }
//
//    public final boolean compareAndSet(float expect, float update) {
//        return bits.compareAndSet(floatToIntBits(expect),
//                floatToIntBits(update));
//    }
//
//    public final void set(float newValue) {
//        bits.set(floatToIntBits(newValue));
//    }
//
//    public final float get() {
//        return intBitsToFloat(bits.get());
//    }
//
//    public float floatValue() {
//        return get();
//    }
//
//    public final float getAndSet(float newValue) {
//        return intBitsToFloat(bits.getAndSet(floatToIntBits(newValue)));
//    }
//
//    public final boolean weakCompareAndSet(float expect, float update) {
//        return bits.weakCompareAndSet(floatToIntBits(expect),
//                floatToIntBits(update));
//    }
//
//    public double doubleValue() {
//        return (double) floatValue();
//    }
//
//}
