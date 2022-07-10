package com.github.doobo.weight;


import com.github.doobo.params.CustomException;
import com.github.doobo.utils.Assert;
import com.github.doobo.utils.CommonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 权重随机算法
 */
public class WeightRandom<T extends Serializable> {

  private static final ThreadLocalRandom random = ThreadLocalRandom.current();

  private List<WeightParent<T>> origin;

  private int sum = 0;

  public WeightRandom(List<WeightParent<T>> list){
    Assert.isTrue(CommonUtils.hasValue(list),"数据源不能为空");
    origin = list;
    setMinAndMaxAndSum();
  }


  /**
   * 概率恒定获取多个随机元素
   */
  public List<WeightParent<T>> getElementsByFixed(int amount){
    List<WeightParent<T>> list = new ArrayList<>(amount);
    for(int i = 0; i < amount; i++) {
		WeightParent<T> weightParent = getRandomElementByBinary();
		if (weightParent == null) {
			continue;
		}
		list.add(WeightParent.newInstance(weightParent));
	}
    return list;
  }

  /**
   * 递减获取随机值，不重复
   */
  public List<WeightParent<T>> getNoRepeatElementsByDecrement(int amount){
    Assert.isTrue(CommonUtils.hasValue(origin),"无数据源");
    long count = origin.stream().filter(Objects::nonNull).filter(f->f.getCount()>0).count();
    Assert.isTrue(count>=amount,"数据源不够！");
    List<WeightParent<T>> backup = null;
    try {
       backup = CommonUtils.cloneCopy(origin);
    } catch (Exception e) {
		throw new CustomException("备份数据源失败，请检测克隆方法");
    }
    List<WeightParent<T>> list = new ArrayList<>(amount);
    for(int i = 0; i < amount; i++){
      WeightParent<T> tmp = getRandomElementByBinary();
	  if(tmp == null){
		continue;
	  }
	  list.add(WeightParent.newInstance(tmp));
      tmp.setCount(0);
      setMinAndMaxAndSum();
    }
    resetData(backup);
    return list;
  }

  /**
   * 恒定概率获取不重复的多个元素
   */
  public List<WeightParent<T>> getNoRepeatElementsByFixed(int amount){
    Assert.isTrue(CommonUtils.hasValue(origin),"无数据源");
    long count = origin.stream().filter(Objects::nonNull).filter(f->f.getCount()>0).count();
    Assert.isTrue(count>=amount,"数据源不够！");
    HashMap<String,WeightParent<T>> result = new HashMap<>(amount);
    int i = 0;
    int max = amount*amount*amount;
    while (result.size() < amount && i < max){
      	WeightParent<T> tmp = getRandomElementByBinary();
		if(tmp == null){
			i++;
			continue;
		}
		result.put(tmp.getUniqueKey(),WeightParent.newInstance(tmp));
      	i++;
    }
    List<WeightParent<T>> list = new ArrayList<>(amount);
    list.addAll(result.values());
    return list;
  }

	/**
	 * 获取原始值
	 */
	public List<WeightParent<T>> getOrigin() {
    return origin;
  }

  /**
   * 获取一个随机元素
   */
  public WeightParent<T> getOneElement(){
    long count = origin.stream().filter(Objects::nonNull).filter(f->f.getCount()>0).count();
    if(count <= 0){
    	throw new CustomException("数据源不够");
	}
    WeightParent<T> weightParent = getRandomElementByBinary();
	if(weightParent == null){
	  throw new CustomException("数据拷贝失败，请检测克隆方法");
	}
	return WeightParent.newInstance(weightParent);

  }

  /**
   * 重置元素
   * @param preTmp
   */
  private void resetData(List<WeightParent<T>> preTmp){
    if(CommonUtils.hasValue(preTmp)){
      this.origin = preTmp;
      resetSum();
    }
  }

  /**
   * 重新计算和
   */
  private void resetSum(){
    if(CommonUtils.hasValue(origin)){
      sum = origin.stream().filter(Objects::nonNull).mapToInt(WeightParent::getCount).sum();
    }
  }

  /**
   * 全部循环获取随机元素
   */
  private WeightParent<T> getWeightElementByAll(){
    int n,m;
    if (sum <= 0) {
      return null;
    }
  	// n in [0, weightSum)
    n = random.nextInt(sum);
    m = 0;
    for (WeightParent<T> o : origin) {
      if (m <= n && n < m + o.getCount()) {
        return o;
      }
      m += o.getCount();
    }
    return null;
  }

  /**
   * 二分法获取随机预测值
   */
  private WeightParent<T> getRandomElementByBinary(){
    int n;
    if (sum <= 0) {
      return null;
    }
  	// n in [0, weightSum)
    n = random.nextInt(sum);
    return binarySearch(origin,n);
  }

  /**
   * 设置边界值
   */
  private void setMinAndMaxAndSum(){
    int m = 0;
    if(CommonUtils.hasValue(origin)) {
      for (WeightParent<T> o : origin) {
        o.setMin(m);
        m += o.getCount();
        o.setMax(m);
      }
      sum = m;
    }
  }

  /**
   * 二分法查找值
   * @param list
   * @param key
   */
  private WeightParent<T> binarySearch(List<WeightParent<T>> list, int key){
    Assert.isTrue(CommonUtils.hasValue(list),"参数异常");
    int lo = 0;
    int hi = list.size() - 1;
    while(lo<=hi){
      int mid = lo + (hi-lo)/2;
      if(key < list.get(mid).getMin()) hi = mid - 1;
      else if(key >= list.get(mid).getMax()) lo = mid + 1;
      else return list.get(mid);
    }
    return null;
  }
}
