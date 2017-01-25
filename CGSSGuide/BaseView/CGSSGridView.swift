//
//  CGSSGridView.swift
//  CGSSGuide
//
//  Created by zzk on 16/7/5.
//  Copyright © 2016年 zzk. All rights reserved.
//

import UIKit

class CGSSGridView: UIView {
    
    var rows: Int
    var columns: Int
    // var borderPixels:CGFloat?
    var grid: [UIView]
    
    subscript (r: Int, c: Int) -> UIView {
        get {
            return grid[r * columns + c]
        }
    }
    
    init(frame: CGRect, rows: Int, columns: Int, views: [UIView]) {
        self.rows = rows
        self.columns = columns
        self.grid = views
        let borderWidth = 1 / UIScreen.main.scale
        let gridWidth = frame.width / CGFloat(columns)
        let gridHeight = frame.height / CGFloat(rows)
        
        super.init(frame: frame)
        
        for i in 0...rows * columns - 1 {
            let view = views[i]
            view.frame = CGRect(x: CGFloat(i % columns) * gridWidth, y: CGFloat(i / columns) * gridHeight, width: gridWidth + borderWidth, height: gridHeight + borderWidth)
            view.layer.borderWidth = borderWidth
            view.layer.borderColor = UIColor.black.cgColor
            self.addSubview(view)
        }
    }
    
    
    override var intrinsicContentSize: CGSize {
        return CGSize.init(width: Screen.width - 20, height: CGFloat(rows) * 18)
    }
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    /*
     // Only override drawRect: if you perform custom drawing.
     // An empty implementation adversely affects performance during animation.
     override func drawRect(rect: CGRect) {
     // Drawing code
     }
     */
    
}
