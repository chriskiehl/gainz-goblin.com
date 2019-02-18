# Gainz Goblin

A quick and dirty no frills weekend Hackathon project for tracking [Wendler 1.2](https://www.t-nation.com/workouts/beyond-531-program-1-2) cycles. 

<p align="center">
  <img src="https://user-images.githubusercontent.com/1408720/52937979-57178600-3315-11e9-8fb3-aae02ed66e17.png">
</p>

The [1.2](https://www.t-nation.com/workouts/beyond-531-program-1-2) flavor of [5/3/1](https://www.t-nation.com/workouts/531-how-to-build-pure-strength) is the best flavor. All other variants are an error. [Gainz Goblin](http://gainz-goblin.com) is for those with the good sense and presence of mind to _not_ engage with lesser variants like 1.1 or... 1.3. 

**Features!**

 * No Customization!
 * Only does one thing! 
 * Green color schemes!
 * Solid name!
 * Cool logo! 
 * Increases your 1RM each cycle for maximum gainz 
 * No ability to export data! 
 * No ability to back-up data! 
 * No ability to transfer data between devices (only uses localStorage)!

**Q:** Whoa, whoa, whoa! That sounds sweet! Where can I try it? 

**A:** Try it here, friendo: [gainz-goblin.com](http://gainz-goblin.com).

<br/>

**Q:** Whoa, whoa, whoa! That logo is sick! Who made it?!

**A:** Sick goblin art via professional cool person [Morgan Zion](http://www.morganzion.co/hello-2/)



## Development

```
lein clean
lein figwheel dev
```

Forward the local port so the websocket can connect

```
ssh -L 3449:localhost:3000 devbox@192.168.0.107
```

## Production Build

To compile clojurescript to javascript:

```
lein clean
lein cljsbuild once min
```

## Deployment

Whole thing is just hosted in a public bucket on S3. Deploymeny is just copying the index.html, css, and js files. 

