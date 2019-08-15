public class Singleton {
    private volatile static Singleton uniqueInstance = null;

    private Singleton(){
        //实例化动作
    }

    public synchronized Singleton getInstance(){
        if (uniqueInstance == null){
            synchronized (Singleton.class){
                if (uniqueInstance == null){
                    return new Singleton();
                }
                return uniqueInstance;
            }
        }else {
            return uniqueInstance;
        }
    }
}
