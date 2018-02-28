package com.kwan.base.rxbus;

import android.support.annotation.NonNull;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


/**
 * Created by Mr.Kwan on 2016-4-5.
 */
public class RxBus {

	private static volatile RxBus defaultInstance;
	// 主题
	//private final Subject<Object, Object> bus;
	// PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
	private List<Subscription> mRXSubscription = new ArrayList<>();

	//整个App 内 所有的 Subject
	private ConcurrentHashMap<Object, List<Subject>> mSubjectMapper = new ConcurrentHashMap<>();


	private RxBus() {}

	// 单例RxBus
	public static RxBus getDefault() {
		RxBus rxBus = defaultInstance;
		if (defaultInstance == null) {
			synchronized (RxBus.class) {
				rxBus = defaultInstance;
				if (defaultInstance == null) {
					rxBus = new RxBus();
					defaultInstance = rxBus;
				}
			}
		}
		return rxBus;
	}

	/**
	 * 订阅RxBus要监听的 数据类型
	 *
	 * @param <T> 类型
	 */
	public <T> Subject<T> registerRxBus(@NonNull Object tag) {

		//Class<T> clz, RxBusSubscriberListener<T> listener

		List<Subject> subjectList = mSubjectMapper.get(tag);

		if (null == subjectList) {
			subjectList = new ArrayList<>();
			mSubjectMapper.put(tag, subjectList);
		}
		Subject<T> subject = PublishSubject.<T>create().toSerialized();
		//使用replaysubject 可以先发送事件,在订阅
		subjectList.add(subject);
		return subject;
	}

	public void unregister(@NonNull Object tag, @NonNull Subject subject) {
		List<Subject> subjects = mSubjectMapper.get(tag);
		if (null != subjects) {
			subjects.remove(subject);
			if (isEmpty(subjects)) {
				mSubjectMapper.remove(tag);
			}
		}
	}

	public boolean isEmpty(List list) {

		if (list != null) {
			if (list.size() == 0) {
				return true;
			} else {
				return false;
			}
		}

		return true;
	}



	/**
	 * 发布事件
	 * @param tag
	 * @param content
	 */
	@SuppressWarnings("unchecked")
	public void post(@NonNull Object tag, @NonNull Object content) {
		List<Subject> subjectList = mSubjectMapper.get(tag);
		if (!isEmpty(subjectList)) {
			//同一个tag 都发布事件
			for (Subject subject : subjectList) {
				subject.onNext(content);
			}
		}
	}

	/**
	 * cuowo
	 * @param tag
	 * @param e
	 */
	@SuppressWarnings("unchecked")
	public void error(@NonNull Object tag, @NonNull Exception e) {
		List<Subject> subjectList = mSubjectMapper.get(tag);
		if (!isEmpty(subjectList)) {
			//同一个tag 都发布事件
			for (Subject subject : subjectList) {
				subject.onError(e);
			}
		}
	}

//	public void postRxBus(Object o) {
//		RxBus.getDefault().post(o);
//	}


}
