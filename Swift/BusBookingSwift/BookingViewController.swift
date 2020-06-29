//
//  BookingViewController.swift
//  BusBookingSwift
//
//  Created by Anil Bunkar on 10/01/19.
//  Copyright © 2019 Adobe Systems. All rights reserved.
//

import UIKit
import ACPCore
import ACPAudience
import ACPUserProfile

class BookingViewController: UIViewController {

    @IBOutlet weak var nonStopButton: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()

        let userIdentifier = ["idName": "userID"]
        ACPCore.collectPii(userIdentifier)
        
        ACPIdentity.syncIdentifier("idName", identifier: "userID1234", authentication: ACPMobileVisitorAuthenticationState.unknown)
        let signals = ["source": "San Francisco", "destination": "Las Vegas"]
        ACPAudience.signal(withData: signals, callback: nil)
    }
    

    @IBAction func nonStopButtonToggled(_ sender: Any) {
        if self.nonStopButton.isSelected {
            self.nonStopButton.isSelected = false
        } else {
            self.nonStopButton.isSelected = true
        }
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
