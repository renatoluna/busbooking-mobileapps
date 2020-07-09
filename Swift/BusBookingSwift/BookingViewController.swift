//
//  BookingViewController.swift
//  BusBookingSwift
//
//  Created by Anil Bunkar on 10/01/19.
//  Copyright © 2019 Adobe Systems. All rights reserved.
//

import UIKit
import ACPCore
import ACPAnalytics
import ACPTarget
//import ACPAudience
import ACPUserProfile

class BookingViewController: UIViewController {

    @IBOutlet weak var nonStopButton: UIButton!
    override func viewDidLoad() {
        var digitalData : [String: String] = [:]
        super.viewDidLoad()

        let userIdentifiers = ["lunaID": "euhv2x83tq"]
        ACPCore.collectPii(userIdentifiers)
        ACPIdentity.syncIdentifiers(userIdentifiers, authentication: ACPMobileVisitorAuthenticationState.authenticated)
        
        digitalData["busBooking.app.name"] = "Bus Booking"
        digitalData["busBooking.app.tech"] = ["Core": ACPCore.extensionVersion(), "AA": ACPAnalytics.extensionVersion(), "AT": ACPTarget.extensionVersion(), "ECID": ACPIdentity.extensionVersion()].map { $0.0 + ":" + $0.1 }.joined(separator: "|")
        digitalData["busBooking.user.profile"] = userIdentifiers["lunaID"]
        digitalData["busBooking.user.authState"] = "authenticated"
        digitalData["busBooking.page.name"] = "Home screen"
        
        ACPCore.trackState("Home screen", data: digitalData)
        
//        let signals = ["pageName": "payment-button/payment-options", "traitsKey": "exampleTraits"]
//        ACPAudience.signal(withData: signals, callback: nil)
        ACPTarget.locationsDisplayed(["booking-view-mbox"], with: nil)
    }
    

    @IBAction func nonStopButtonToggled(_ sender: Any) {
        var digitalData : [String: String] = [:]
        digitalData["busBooking.page.button.name"] = "No Stops"
        if self.nonStopButton.isSelected {
            self.nonStopButton.isSelected = false
        } else {
            self.nonStopButton.isSelected = true
        }
        digitalData["busBooking.page.button.value"] = self.nonStopButton.isSelected.description
        ACPCore.trackAction("No stops button interaction", data: digitalData)
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
