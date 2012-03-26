//
//  MyClass.h
//  WebView
//
//  Created by Amrit Mishra on 6/25/11.
//  Copyright 2011 Vimukti Technologies. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "WebKit/WebKit.h"
#import "Reachability.h"
#import "CachedUrlProtocol.h"
#import "AccounterMenuCreator.h"

@class Reachability;

@interface MyClass : NSObject<NSXMLParserDelegate,NSWindowDelegate> {
@private
    
    IBOutlet NSWindow *mainWindow;
    IBOutlet WebView *webPage;
    IBOutlet NSProgressIndicator *progressbar;
    IBOutlet NSProgressIndicator *longProgressBar;
    IBOutlet NSMenu *mainMenu;
    IBOutlet NSMenu *helpMenu;
    IBOutlet NSMenu *accounterMenu;
    

    BOOL webPageLoadingComplete;
    BOOL urlAdded;
    BOOL _loading;

    BOOL applicationOpen;
    BOOL localServer;
    
    
    IBOutlet NSPanel *helpPanel;
    

    int inc;
    int countingTimer;
    
    NSTimer *loadMenuTimer;
    
    NSMutableArray *itemName;
    NSMutableArray *itemLinks;
    NSMutableArray *str;
    NSString *mainPathLocation;
    NSMutableURLRequest* mainRequest;
    NSString *downloadPath;
    BOOL createWebScript;
    
    Reachability* internetReachable;
    Reachability* hostReachable;
    
    //opening ulr controls
    IBOutlet NSPanel *openingURLpanel;
    IBOutlet NSTextField *localaddressTextField;
    IBOutlet NSButton *checkButton;
    AccounterMenuCreator * menuCreator;
    
}

-(void) reloadMenu;
-(void) LoadOpeningURL :(NSString *)url;
-(void) checkNetworkStatus:(NSNotification *)notice;
-(void)clearMenu;
-(void)changeWebPageLink:(NSString*)link;

- (IBAction)copyWebviewText:(id)sender;
- (IBAction)pasteWebviewText:(id)sender;

- (IBAction)sendFeedback:(id)sender;
- (IBAction)clearCache:(id)sender;
- (IBAction)openHelp:(id)sender;
- (IBAction)closeHelpPanel:(id)sender;
- (IBAction)gpOnline:(id)sender;



/* Download Management Methods */
- (void)presentDownloadSuccess;
- (void)presentDownloadError;

- (IBAction)lanchUrlSelected:(id)sender;
- (IBAction)changeLaunchUrl:(id)sender;
-(void) InitiateURL :(NSString *)url;

@end