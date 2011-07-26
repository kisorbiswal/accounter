
<script language="javascript" type="text/javascript">

window.domain=location.hostname.toString();
//window.domain='${dataDomain}';
var reconnectInterval=1000;
Meteor = {

         callbacks: {
                process: function() {},
                reset: function() {},
                eof: function() {},
                statuschanged: function() {},
                changemode: function() {},
                syncstatuschanged: function() {}
        },
        channelcount: 0,
        channels: {},
        debugmode: true,
        frameref: null,
        host: window.domain,
        hostid: null,
        mode: "stream",
        pingtimeout: 20000,
        port: location.port,//"${port}",
        recvtimes: [],
        lastrequest: null,
        status: 0,
        syncstatus: 1,
        protocol:location.protocol,//"${protocol}",
         isSupportedBrowser: function() {
                var v;
                if (v = navigator.userAgent.match(/compatible\; MSIE\ ([0-9\.]+)\;/i)) {
                        if (parseFloat(v[1]) <= 5.5) return false;
                } else if (v = navigator.userAgent.match(/Gecko\/([0-9]+)/i)) {
                        if (parseInt(v[1]) <= 20051015) return false;
                } else if (v = navigator.userAgent.match(/WebKit\/([0-9\.]+)/i)) {
                        if (parseFloat(v[1]) < 400) return false;
                }
                return true;
        },

        register: function(ifr) {
                ifr.p = Meteor.process;
                ifr.r = Meteor.reset;
                ifr.r1 = Meteor.reset1;
                ifr.r2 = Meteor.reset2;
                ifr.eof = Meteor.eof;
                ifr.ch = Meteor.channelInfo;
                clearTimeout(Meteor.frameloadtimer);
                Meteor.setstatus(4);
                Meteor.log("Frame registered");
                Meteor.pingtimer = setTimeout(Meteor.pingtimeout, 10000);
        },

        joinChannel: function(channelname, backtrack) {
                //if (typeof(Meteor.channels[channelname]) != "undefined") throw "Cannot join channel "+channelname+": already subscribed";
                if (typeof(Meteor.channels[channelname]) != "undefined") return;
                Meteor.channels[channelname] = {backtrack:backtrack};
                Meteor.log("Joined channel "+channelname);
                Meteor.channelcount++;
                if (Meteor.status != 0 && Meteor.status != 6) Meteor.connect();
        },

        leaveChannel: function(channelname) {
                if (typeof(Meteor.channels[channelname]) == "undefined") throw "Cannot leave channel "+channelname+": not subscribed";
                delete Meteor.channels[channelname];
                Meteor.log("Left channel "+channelname);
                Meteor.channelcount--;
                if (Meteor.channelcount && Meteor.status != 0 && Meteor.status != 6) Meteor.connect();
                else Meteor.disconnect();
        },

        connect: function() {
                if (!Meteor.host) throw "Meteor host not specified";
                if (isNaN(Meteor.port)) throw "Meteor port not specified";
                if (!Meteor.channelcount) throw "No channels specified";
                if (Meteor.status) Meteor.disconnect();
                Meteor.log("Connecting");
                Meteor.setstatus(1);
                if (!Meteor.hostid) Meteor.hostid = Meteor.time()+""+Math.floor(Math.random()*1000000)
                if (Meteor.mode=="stream") Meteor.mode = Meteor.selectStreamTransport();
                Meteor.log("Selected "+Meteor.mode+" transport");
                if (Meteor.mode=="xhrinteractive" || Meteor.mode=="iframe" || Meteor.mode=="serversent") {
                        if (Meteor.mode == "iframe") {
                                Meteor.loadFrame(Meteor.getSubsUrl());
                        } else {
                        	  //document.domain=Meteor.extract_xss_domain(document.domain);
                              Meteor.loadFrame(Meteor.protocol+"//"+Meteor.host+((Meteor.port=="") ? "" : ":"+Meteor.port)+"/stream.jsp");                              
                        }
                        clearTimeout(Meteor.pingtimer);
                } 
        },

        disconnect: function() {
                if (Meteor.status) {
                        if (Meteor.status != 6) Meteor.setstatus(0);
                        clearTimeout(Meteor.pingtimer);
                        clearTimeout(Meteor.frameloadtimer);
                        Meteor.setsyncstatus(1);
                        if (typeof CollectGarbage == 'function') CollectGarbage();
                        Meteor.log("Disconnected");
                        try { Meteor.frameref.parentNode.removeChild(Meteor.frameref); delete Meteor.frameref; return true; } catch(e) { }
                        try { Meteor.frameref.open(); Meteor.frameref.close(); return true; } catch(e) {}                      
                }
        },
       
        selectStreamTransport: function() {
                try {
                        var test = ActiveXObject;
                        return "iframe";
                } catch (e) {}
                if ((typeof window.addEventStream) == "function") return "iframe";
                return "xhrinteractive";
        },

        getSubsUrl: function() {
                var host = Meteor.host;

              
                var surl = Meteor.protocol+"//" + host + ((Meteor.port=="" )?"":":"+Meteor.port) + "/do.comet/"+ Meteor.mode;
                surl += "?nc="+Meteor.time();
                return surl;
        },

        loadFrame: function(url) {
                try {
                        if (!Meteor.frameref) {
                                var transferDoc = new ActiveXObject("htmlfile");
                                Meteor.frameref = transferDoc;
                        }
                        Meteor.frameref.open();
                        Meteor.frameref.write("<html><script>");
                        Meteor.frameref.write("document.domain=\""+(document.domain)+"\";");
                        Meteor.frameref.write("</"+"script></html>");
                        Meteor.frameref.parentWindow.Meteor = Meteor;
                        Meteor.frameref.close();
                        var ifrDiv = Meteor.frameref.createElement("div");
                        Meteor.frameref.appendChild(ifrDiv);
                    
                        ifrDiv.innerHTML = "<iframe src=\""+url+"\"></iframe>";
                } catch (e) {
                        if (!Meteor.frameref) {
                                var ifr = document.createElement("IFRAME");
                                ifr.style.width = "10px";
                                ifr.style.border = "none";
                                ifr.style.position = "absolute";
                                ifr.style.top = "-20px";
                                ifr.style.marginTop = "-20px";
                                ifr.style.zIndex = "-20";
                                ifr.Meteor = Meteor;
                                document.body.appendChild(ifr);
                                Meteor.frameref = ifr;
                        }
                        Meteor.frameref.setAttribute("src", url);
                }
        
                Meteor.log("Loading URL '"+url+"' into frame...");
                Meteor.frameloadtimer = setTimeout(Meteor.frameloadtimeout, 5000);
        },

        pollmode: function() {
                Meteor.log("Ping timeout");
                        Meteor.lastpingtime = false;
              
        },

        process: function(id, channel, data) {
                if (id == -1) {
                  if(data != ''){
                	    Meteor.setsyncstatus(parseInt(data));
                    }
                        Meteor.log("Ping");
                        Meteor.ping();
                } else if (typeof(Meteor.channels[channel]) != "undefined") {
                        Meteor.log("Message "+id+" received on channel "+channel+" (last id on channel: "+Meteor.channels[channel].lastmsgreceived+")\n"+data);
                        Meteor.callbacks["process"](channel+":"+data);
                        Meteor.channels[channel].lastmsgreceived = id;
                   
                }
                Meteor.setstatus(5);
        },


        ping: function() {
                if (Meteor.pingtimer) {
                        clearTimeout(Meteor.pingtimer);
                        Meteor.lastpingtime = Meteor.time();
                        Meteor.pingtimer = setTimeout(Meteor.pingtimeout, 10000);
                }
                Meteor.setstatus(5);
                reconnectInterval=1000;
        },

        reset: function() {
                if (Meteor.status != 6 && Meteor.status != 0) {
                        Meteor.log("Stream reset");
                        Meteor.ping();
                        Meteor.callbacks["reset"]();
                        setTimeout(Meteor.connect, reconnectInterval);
                        reconnectInterval+=1000;
                }
        },
        reset1: function() {
                if (Meteor.status != 6 && Meteor.status != 0) {
                        Meteor.log("Stream reset1");
                        Meteor.ping();
                        Meteor.callbacks["reset"]();
                        setTimeout(Meteor.connect, reconnectInterval);
                        reconnectInterval+=1000;
                }
        },
        reset2: function() {
                if (Meteor.status != 6 && Meteor.status != 0) {
                        Meteor.log("Stream reset2");
                        Meteor.ping();
                        Meteor.callbacks["reset"]();
                        setTimeout(Meteor.connect, reconnectInterval);
                        reconnectInterval+=1000;
                }
        },

        eof: function() {
                Meteor.log("Received end of stream, will not reconnect");
                Meteor.setstatus(6);
                Meteor.callbacks["eof"]();
                Meteor.disconnect();
        },

        channelInfo: function(channel, id) {
                Meteor.channels[channel].lastmsgreceived = id;
                Meteor.log("Received channel info for channel "+channel+": resume from "+id);
        },

 

        registerEventCallback: function(evt, funcRef) {
                Function.prototype.andThen=function(g) {
                        var f=this;
                        var a=Meteor.arguments
                        return function(args) {
                                f(a);g(args);
                        }
                };
                if (typeof Meteor.callbacks[evt] == "function") {
                        Meteor.callbacks[evt] = (Meteor.callbacks[evt]).andThen(funcRef);
                } else {
                        Meteor.callbacks[evt] = funcRef;
                }
        },

        frameloadtimeout: function() {
                Meteor.log("Frame load timeout");
                if (Meteor.frameloadtimer) clearTimeout(Meteor.frameloadtimer);
                Meteor.setstatus(3);
                Meteor.connect();
        },
        pingtimeout: function() {
        		Meteor.log("Ping timeout");
        		if (Meteor.pingtimer) clearTimeout(Meteor.pingtimer);
                Meteor.connect();
        },

        extract_xss_domain: function(old_domain) {
                if (old_domain.match(/^(\d{1,3}\.){3}\d{1,3}$/)) return old_domain;
                domain_pieces = old_domain.split('.');
                return domain_pieces.slice(-2, domain_pieces.length).join(".");
        },

        setstatus: function(newstatus) {
                  if (Meteor.status != newstatus) {
                        Meteor.status = newstatus;
                        Meteor.callbacks["statuschanged"](newstatus);
                }
        },
        
        setsyncstatus: function(newstatus) {
        	    if (Meteor.syncstatus != newstatus) {
        		 Meteor.syncstatus = newstatus;
        	     Meteor.callbacks["syncstatuschanged"](newstatus);
        	 }
        },

        log: function(logstr) {
                if (Meteor.debugmode) {
                        if (window.console) {
                                window.console.log(logstr);
                        } else if (document.getElementById("meteorlogoutput")) {
                                document.getElementById("meteorlogoutput").innerHTML += logstr+"<br/>";
                        }
                }
        },



   

        time: function() {
                var now = new Date();
                return now.getTime();
        }
}

var oldonunload = window.onunload;
if (typeof window.onunload != 'function') {
        window.onunload = Meteor.disconnect;
} else {
        window.onunload = function() {
                if (oldonunload) oldonunload();
                Meteor.disconnect();
        }
}

</script>


