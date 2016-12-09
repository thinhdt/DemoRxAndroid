package realm.demo.com.demorxandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.R.attr.value;
import static rx.Observable.from;


public class MainActivity extends AppCompatActivity {

    private  int mCounter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Integer[] items = {0, 1, 2, 3, 4};

        mapObservable(items);
        //filterObservable(items);
        //fromObservable(items);
        //justObserable();
        //createSubscription();
        //deferObserver();
        //observeOn();
        //subscribeOn();
        //multipleSubscribeOn();
    }


    private void mapObservable(Integer[] items) {
        Observable.just(1, 2, 3, 4, 5).map(new Func1<Integer, Integer>() {

            @Override
            public Integer call(Integer integer) {
                return integer * 10;
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("Complete!");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError!");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext!" + integer);
            }
        });
    }


    private void filterObservable(Integer[] items) {
        Observable.just(1, 2, 3, 4, 5).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer < 4;
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("Complete!");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError!");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext!" + integer);
            }
        });
    }

    private void fromObservable(Integer[] items) {
        Observable.from(items).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("Complete!");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError!");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext!" + integer);
            }
        });
    }

    private void justObserable() {
        Observable.just(1, 2, 3)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("Complete!");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext!: " + integer);
                    }
                });
    }

    private void deferObserver() {
        SomeType instance = new SomeType();
        Observable<String> value = instance.valueObservable();
        instance.setValue("Some Value");
        value.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.d("Thinhdt", "Defer Observable: " + s);
            }
        });
    }

    private void createSubscription() {
        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        Log.d("Observable thread", Thread.currentThread().getName());
                        sub.onNext("Hello World");
                        sub.onCompleted();
                    }
                }
        );

        Observer myObserver = new Observer<String>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String text) {
                Log.d("Observer thread", Thread.currentThread().getName());
                System.out.println(text);
                Log.d("Thinhdt", text);
            }
        };

        myObservable.subscribe(myObserver);
    }

    private void observeOn () {
        getANumberObservable()
                //.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        Log.i("Operator thread", Thread.currentThread().getName());
                        return String.valueOf(integer);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i("Subscriber thread", Thread.currentThread().getName() + " onNext: " + s);
                    }
                });
    }

    private void subscribeOn(Scheduler computation) {
        getANumberObservable()
                .subscribeOn(Schedulers.io())
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        Log.i("Operator thread", Thread.currentThread().getName());
                        return String.valueOf(integer);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i("Subscriber thread", Thread.currentThread().getName() + " onNext: " + s);
                    }
                });
    }

    private void multipleSubscribeOn() {
        Observable.just("Some String")
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return s.length();
                    }
                })
                .subscribeOn(Schedulers.computation()) // changing to computation
                .subscribeOn(Schedulers.io()) // won’t change the thread to IO
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext!" + integer);
                        Log.d("onNext", "Number " + integer + " Thread: " + Thread.currentThread().getName());
                    }
                });
    }

    private Observable<Integer> getANumberObservable() {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override public Observable<Integer> call() {
                Log.i("Observable thread", Thread.currentThread().getName());
                //return Observable.from(new Integer[] {1, 2, 3});
                return Observable.just(1);
            }
        });
    }
}
