```shell
λ jstack 15256                                                                                                                                 
2023-09-19 16:16:04                                                                                                                            
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.181-b13 mixed mode):                                                                    
                                                                                                                                               
"DestroyJavaVM" #13 prio=5 os_prio=0 tid=0x0000000003343000 nid=0x6fc waiting on condition [0x0000000000000000]                                
   java.lang.Thread.State: RUNNABLE                                                                                                            
                                                                                                                                               
"Thread-1" #12 prio=5 os_prio=0 tid=0x0000000023971000 nid=0x45bc waiting for monitor entry [0x000000002476f000]                               
   java.lang.Thread.State: BLOCKED (on object monitor)                                                                                         
        at com.qinchy.deadlock.EasyJstackResource.read(EasyJstackResource.java:19)                                                             
        - waiting to lock <0x0000000740cfd400> (a com.qinchy.deadlock.Resource)                                                                
        - locked <0x0000000740cfd3f0> (a com.qinchy.deadlock.Resource)                                                                         
        at com.qinchy.deadlock.EasyJstack.run(EasyJstack.java:25)                                                                              
                                                                                                                                               
"Thread-0" #11 prio=5 os_prio=0 tid=0x000000002396e800 nid=0x123c waiting for monitor entry [0x000000002466f000]                               
   java.lang.Thread.State: BLOCKED (on object monitor)                                                                                         
        at com.qinchy.deadlock.EasyJstackResource.write(EasyJstackResource.java:29)                                                            
        - waiting to lock <0x0000000740cfd3f0> (a com.qinchy.deadlock.Resource)                                                                
        - locked <0x0000000740cfd400> (a com.qinchy.deadlock.Resource)                                                                         
        at com.qinchy.deadlock.EasyJstack.run(EasyJstack.java:26)                                                                              
                                                                                                                                               
"Service Thread" #10 daemon prio=9 os_prio=0 tid=0x000000002394c000 nid=0x46b8 runnable [0x0000000000000000]                                   
   java.lang.Thread.State: RUNNABLE                                                                                                            
                                                                                                                                               
"C1 CompilerThread3" #9 daemon prio=9 os_prio=2 tid=0x0000000023899000 nid=0x19fc waiting on condition [0x0000000000000000]                    
   java.lang.Thread.State: RUNNABLE                                                                                                            
                                                                                                                                               
"C2 CompilerThread2" #8 daemon prio=9 os_prio=2 tid=0x0000000023895000 nid=0x3c9c waiting on condition [0x0000000000000000]                    
   java.lang.Thread.State: RUNNABLE                                                                                                            
                                                                                                                                               
"C2 CompilerThread1" #7 daemon prio=9 os_prio=2 tid=0x0000000023892000 nid=0x4154 waiting on condition [0x0000000000000000]                    
   java.lang.Thread.State: RUNNABLE                                                                                                            
                                                                                                                                               
"C2 CompilerThread0" #6 daemon prio=9 os_prio=2 tid=0x000000002383b800 nid=0x39fc waiting on condition [0x0000000000000000]                    
   java.lang.Thread.State: RUNNABLE                                                                                                            
                                                                                                                                               
"Attach Listener" #5 daemon prio=5 os_prio=2 tid=0x000000002383a800 nid=0x3d04 waiting on condition [0x0000000000000000]                       
   java.lang.Thread.State: RUNNABLE                                                                                                            
                                                                                                                                               
"Signal Dispatcher" #4 daemon prio=9 os_prio=2 tid=0x0000000023890000 nid=0x3418 runnable [0x0000000000000000]                                 
   java.lang.Thread.State: RUNNABLE                                                                                                            
                                                                                                                                               
"Finalizer" #3 daemon prio=8 os_prio=1 tid=0x0000000003439800 nid=0x4358 in Object.wait() [0x0000000023cff000]                                 
   java.lang.Thread.State: WAITING (on object monitor)                                                                                         
        at java.lang.Object.wait(Native Method)                                                                                                
        - waiting on <0x0000000740c08ed0> (a java.lang.ref.ReferenceQueue$Lock)                                                                
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:144)                                                                        
        - locked <0x0000000740c08ed0> (a java.lang.ref.ReferenceQueue$Lock)                                                                    
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:165)                                                                        
        at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:216)                                                                     
                                                                                                                                               
"Reference Handler" #2 daemon prio=10 os_prio=2 tid=0x0000000003438000 nid=0x798 in Object.wait() [0x00000000237ff000]                         
   java.lang.Thread.State: WAITING (on object monitor)                                                                                         
        at java.lang.Object.wait(Native Method)                                                                                                
        - waiting on <0x0000000740c06bf8> (a java.lang.ref.Reference$Lock)                                                                     
        at java.lang.Object.wait(Object.java:502)                                                                                              
        at java.lang.ref.Reference.tryHandlePending(Reference.java:191)                                                                        
        - locked <0x0000000740c06bf8> (a java.lang.ref.Reference$Lock)                                                                         
        at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)                                                                    
                                                                                                                                               
"VM Thread" os_prio=2 tid=0x0000000021928800 nid=0x17e4 runnable                                                                               
                                                                                                                                               
"GC task thread#0 (ParallelGC)" os_prio=0 tid=0x0000000003358000 nid=0xbe8 runnable                                                            
                                                                                                                                               
"GC task thread#1 (ParallelGC)" os_prio=0 tid=0x0000000003359800 nid=0x4148 runnable                                                           
                                                                                                                                               
"GC task thread#2 (ParallelGC)" os_prio=0 tid=0x000000000335b000 nid=0x3c4c runnable                                                           
                                                                                                                                               
"GC task thread#3 (ParallelGC)" os_prio=0 tid=0x000000000335d800 nid=0x3944 runnable                                                           
                                                                                                                                               
"GC task thread#4 (ParallelGC)" os_prio=0 tid=0x000000000335f800 nid=0x3034 runnable                                                           
                                                                                                                                               
"GC task thread#5 (ParallelGC)" os_prio=0 tid=0x0000000003361000 nid=0x3b48 runnable                                                           
                                                                                                                                               
"GC task thread#6 (ParallelGC)" os_prio=0 tid=0x0000000003364000 nid=0x2db8 runnable                                                           
                                                                                                                                               
"GC task thread#7 (ParallelGC)" os_prio=0 tid=0x0000000003365000 nid=0x42f8 runnable                                                           
                                                                                                                                               
"VM Periodic Task Thread" os_prio=2 tid=0x0000000023952000 nid=0x243c waiting on condition                                                     
                                                                                                                                               
JNI global references: 5                                                                                                                       
                                                                                                                                               
                                                                                                                                               
Found one Java-level deadlock:                                                                                                                 
=============================                                                                                                                  
"Thread-1":                                                                                                                                    
  waiting to lock monitor 0x0000000021930c38 (object 0x0000000740cfd400, a com.qinchy.deadlock.Resource),                                      
  which is held by "Thread-0"                                                                                                                  
"Thread-0":                                                                                                                                    
  waiting to lock monitor 0x0000000021932188 (object 0x0000000740cfd3f0, a com.qinchy.deadlock.Resource),                                      
  which is held by "Thread-1"                                                                                                                  
                                                                                                                                               
Java stack information for the threads listed above:                                                                                           
===================================================                                                                                            
"Thread-1":                                                                                                                                    
        at com.qinchy.deadlock.EasyJstackResource.read(EasyJstackResource.java:19)                                                             
        - waiting to lock <0x0000000740cfd400> (a com.qinchy.deadlock.Resource)                                                                
        - locked <0x0000000740cfd3f0> (a com.qinchy.deadlock.Resource)                                                                         
        at com.qinchy.deadlock.EasyJstack.run(EasyJstack.java:25)                                                                              
"Thread-0":                                                                                                                                    
        at com.qinchy.deadlock.EasyJstackResource.write(EasyJstackResource.java:29)                                                            
        - waiting to lock <0x0000000740cfd3f0> (a com.qinchy.deadlock.Resource)                                                                
        - locked <0x0000000740cfd400> (a com.qinchy.deadlock.Resource)                                                                         
        at com.qinchy.deadlock.EasyJstack.run(EasyJstack.java:26)                                                                              
                                                                                                                                               
Found 1 deadlock.                                                                                                                              
```