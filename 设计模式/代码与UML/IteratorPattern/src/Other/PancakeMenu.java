//public class PancakeMenu implements Menu{
//    private final int MAX_ITEM = 100;
//    private MenuItem[] menuItems = new MenuItem[MAX_ITEM];
//    private int currentItem = 0;
//
//    @Override
//    public PancakeMenuIterator createIterator() {
//        return new PancakeMenuIterator(menuItems);
//    }
//
//    @Override
//    public void addItem(MenuItem item) {
//        if (currentItem < MAX_ITEM){
//            menuItems[currentItem] = item;
//            currentItem++;
//        }else {
//            throw new ArrayIndexOutOfBoundsException("Can't add more items");
//        }
//    }
//}
