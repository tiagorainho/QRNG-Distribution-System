package qrng.QrngService.RandomNumbers;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Repository;

@Repository("in_memory")
public class RandomNumberInMemoryAccessService implements RandomNumbersDao {

    private Map<String, Queue<Byte>> generators = new HashMap<>();
    private int lastByteUsage = 0;
    private final byte[] stairAllOnes = new byte[] {
        127, 63, 31, 15, 7, 3, 1
    };

    public RandomNumberInMemoryAccessService() {  }

    @Override
    public void add(String generator, List<Byte> bits) {
        if(generators.containsKey(generator)) {
            generators.get(generator).addAll(bits);
        }
        else {
            generators.put(generator, new ConcurrentLinkedQueue<>(bits));
        }
    }

    @Override
    public BigInteger pop(String generator, int bitsToRetrieve) {
        Queue<Byte> queue = generators.get(generator);
        Byte b;
        byte bytes;
        BigInteger big = BigInteger.ZERO;
        while(bitsToRetrieve > 0) {
            int shiftLeft = 0;
            if(bitsToRetrieve < Byte.SIZE) {
                if(bitsToRetrieve >= Byte.SIZE - this.lastByteUsage) {
                    b = queue.remove();
                    bytes = (byte)(b.byteValue()<<this.lastByteUsage);
                    bytes = (byte) (bytes >> this.lastByteUsage);
                    if(this.lastByteUsage > 0) {
                        bytes = (byte) (bytes & stairAllOnes[this.lastByteUsage-1]);
                    }
                    shiftLeft = this.lastByteUsage;
                    bitsToRetrieve = bitsToRetrieve - (Byte.SIZE - this.lastByteUsage);
                    this.lastByteUsage = 0;
                }
                else {
                    b = queue.peek();
                    bytes = (byte)(b.byteValue()<<(this.lastByteUsage));
                    bytes = (byte)(b.byteValue()>>(Byte.SIZE-(bitsToRetrieve+this.lastByteUsage)));
                    bytes = (byte) (bytes & stairAllOnes[Byte.SIZE-(bitsToRetrieve+this.lastByteUsage)-1]);
                    
                    shiftLeft = bitsToRetrieve;
                    this.lastByteUsage += bitsToRetrieve;
                    bitsToRetrieve = 0;
                }
            }
            else {
                b = queue.remove();
                if(this.lastByteUsage > 0) {
                    bytes = (byte)(b.byteValue()<<this.lastByteUsage);
                    bytes = (byte)(bytes>>(this.lastByteUsage));
                    bytes = (byte) (bytes & stairAllOnes[this.lastByteUsage-1]);

                    bitsToRetrieve = bitsToRetrieve - (Byte.SIZE - this.lastByteUsage);
                    shiftLeft = bitsToRetrieve;
                    this.lastByteUsage = 0;
                }
                else {
                    bytes = b.byteValue();
                    shiftLeft = Byte.SIZE;
                    bitsToRetrieve -= Byte.SIZE;
                }
                
                //System.out.println(Integer.toBinaryString((int)bytes)  + " ......");
            }
            int i2 = Byte.toUnsignedInt(bytes);
            String x = String.valueOf(i2);
            big = big.add(new BigInteger(x));
            if(bitsToRetrieve > 0) {
                big = big.shiftLeft(Math.min(shiftLeft, bitsToRetrieve));
            }
            
            //System.out.println("Valor: " + big.toString(2));
        }
        System.out.println("Final value from " + generator + ": " + big.toString(2));

        /*
        byte[] byteArray = new byte[ bitsToRetrieve / Byte.SIZE + ((bitsToRetrieve % Byte.SIZE > 0)?1:0) +1 ];
        Byte b;
        for(int i=0; bitsToRetrieve > 0; i++) {
            if(bitsToRetrieve < Byte.SIZE) {
                if(bitsToRetrieve > Byte.SIZE - this.lastByteUsage) {
                    b = queue.remove();
                    byteArray[i] = (byte)(b.byteValue()<<this.lastByteUsage);
                    bitsToRetrieve = bitsToRetrieve - (Byte.SIZE - this.lastByteUsage);
                    this.lastByteUsage = 0;
                }
                else {  // ERRRO AQUII !!
                    System.out.println("----------2");
                    b = queue.peek();
                    // eliminate right side bits
                    b = (byte)(b.byteValue()>>(Byte.SIZE-(bitsToRetrieve+this.lastByteUsage)));
                    // eliminate left side bits
                    byteArray[i] = (byte)(b<<(Byte.SIZE-(bitsToRetrieve+this.lastByteUsage*2)));
                    this.lastByteUsage += bitsToRetrieve;
                    bitsToRetrieve = 0;
                    System.out.println(byteArray[i] + "ppppppppp");
                }// -1110100 -> -116
            }
            else {
                System.out.println("----------3");
                // we can pick and remove the whole byte without losing information
                b = queue.remove();
                if(this.lastByteUsage > 0) {
                    byteArray[i] = (byte)(b.byteValue()>>this.lastByteUsage);
                    this.lastByteUsage = 0;
                }
                else {
                    byteArray[i] = b.byteValue();
                }
                bitsToRetrieve -= Byte.SIZE;
            }
            System.out.println("---- " + new BigInteger(byteArray, 0, i+1));
        }
        BigInteger a = new BigInteger(byteArray, 0, byteArray.length);
        System.out.println("Valor: " + a.toString(2));
        */
        //System.out.println("Valor: " + big.toString(2));
        return big;
    }

    @Override
    public int size(String generator) {
        if(this.generators.containsKey(generator)) {
            return this.generators.get(generator).size();
        }
        return 0;
    }

    
}



        /*
        for(int i=0; i<byteArray.length; i++) {
            if(i == 0) {
                // first
                b = queue.remove();
            }
            else if(i == byteArray.length-1) {
                // last
                b = queue.peek();
            }
            else{
                // middle
                byteArray[i] = queue.remove();
            }
        }
        */

