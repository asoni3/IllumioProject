# IllumioProject

1. **Functionality**:   
I tested my code by giving a CSV file containing rules as a input file. I tested against the given test cases and additional by calling the `accept_packet()` function from `main()` method. 

2. **Code Clarity and Cleanliness**:   
I tried to wrap up my submission in the time period. I was not able to finish cleaning up and commenting where necessary. There are lot of gaps for improvements in the current code.

3. **Performance**:   
I have currently implemented non-optimal solution. However, I have thought through faster approaches which I can implement in additional time.

**Approach: Merging IP Address Ranges and Port Ranges** (Better Space & Time Complexity)

Considering there would be large number of rules, we can merge in following fashion:
1. Inbound   
  a. TCP
  List - Containing all ports/ranges of ports
  List - Containing all ips/ranges of ips
  
  Here, we will be merging ips and making a range as well as merging ip addresses
  For ex:   
  192.168.1.10 - 192.168.2.10 and 192.168.1.5 - 192.168.1.15 -> can be merged to 192.168.1.5 - 192.168.2.10
  For the new range of IPs, we will also merge the ports/ranges
  
  b. UDP

2. Outbound:   
  a. TCP
  
  b. UDP
  
  So, there will be total of 4 items (Inbound UDP, Inbound TCP, Outbound TCP, Outbound UDP) and each with list of ip ranges and port ranges.
  
 We will be processing the rules only once to form this new organized list and each time `accept_packet()` is called, we will only go through this new list which will be more faster. 
 
 
