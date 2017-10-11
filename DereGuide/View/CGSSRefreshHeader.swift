//
//  CGSSRefreshHeader.swift
//  DereGuide
//
//  Created by zzk on 11/10/2017.
//  Copyright © 2017 zzk. All rights reserved.
//

import UIKit
import MJRefresh

class CGSSRefreshHeader: MJRefreshHeader {
    
    let loadingView = UIActivityIndicatorView(activityIndicatorStyle: .gray)
    let arrowImage = UIImageView()
    
    override var state: MJRefreshState {
        didSet {
            switch state {
            case .idle:
                loadingView.isHidden = true
                arrowImage.isHidden = false
                loadingView.stopAnimating()
            case .pulling:
                loadingView.isHidden = false
                arrowImage.isHidden = true
                loadingView.startAnimating()
            case .refreshing:
                loadingView.isHidden = false
                arrowImage.isHidden = true
                loadingView.startAnimating()
            default:
                break
            }
        }
    }
    
    override func prepare() {
        super.prepare()
        frame.size.height = 50
        addSubview(loadingView)
        
        arrowImage.image = #imageLiteral(resourceName: "arrow-downward").withRenderingMode(.alwaysTemplate)
        addSubview(arrowImage)
        
        arrowImage.tintColor = .gray
    }
    
    override func placeSubviews(){
        super.placeSubviews()
        loadingView.center = CGPoint(x: frame.width / 2, y: frame.height / 2)
        arrowImage.frame = CGRect(x: 0, y: 0, width: 24, height: 24)
        arrowImage.center = loadingView.center
    }
    
}

