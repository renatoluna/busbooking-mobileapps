//
//  BookingDetailsViewController.swift
//  BusBookingSwift
//
//  Created by Anil Bunkar on 10/01/19.
//  Copyright © 2019 Adobe Systems. All rights reserved.
//

import UIKit
import ACPCore
import ACPTarget

class BookingDetailsViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        var digitalData : [String: String] = [:]
            digitalData["busBooking.page.name"] = "Booking Details"
            digitalData["&&products"] = ["24D334", "Chartered Bus", "Volvo A/C Multi Axle (2+2)", "3", "1900"].joined(separator: ";")
            digitalData["&&events"] = "prodView"

        ACPCore.trackState("Booking Details", data: digitalData)
        
        let product = ACPTargetProduct(id: "24D334", categoryId: "Chartered Bus")
        let targetParameters = ACPTargetParameters(parameters: nil, profileParameters: nil, product: product, order: nil)
        
        ACPTarget.locationsDisplayed(["bookingDetails"], with: targetParameters)
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
