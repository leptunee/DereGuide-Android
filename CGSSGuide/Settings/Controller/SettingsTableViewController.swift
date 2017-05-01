//
//  SettingsTableViewController.swift
//  CGSSGuide
//
//  Created by zzk on 16/7/15.
//  Copyright © 2016年 zzk. All rights reserved.
//

import UIKit
import MessageUI
import SDWebImage

class SettingsTableViewController: UITableViewController, UpdateStatusViewDelegate {
    
    @IBOutlet weak var downloadAtStartCell: UITableViewCell! {
        didSet {
            let downloadAtStartSwitch = UISwitch()
            downloadAtStartCell.accessoryView = downloadAtStartSwitch
            let downloadAtStart = UserDefaults.standard.value(forKey: "DownloadAtStart") as? Bool ?? true
            downloadAtStartSwitch.isOn = downloadAtStart
            downloadAtStartSwitch.addTarget(self, action: #selector(downloadAtStartValueChanged), for: .valueChanged)
        }
    }
    
    @IBOutlet weak var fullImageCacheCell: UITableViewCell! {
        didSet {
            let fullImageCacheSwitch = UISwitch()
            fullImageCacheCell.accessoryView = fullImageCacheSwitch
            let fullImageCache = UserDefaults.standard.value(forKey: "FullImageCache") as? Bool ?? true
            fullImageCacheSwitch.isOn = fullImageCache
            fullImageCacheSwitch.addTarget(self, action: #selector(fullImageCacheChanged), for: .valueChanged)
        }
    }
    func fullImageCacheChanged(_ sender: UISwitch) {
        UserDefaults.standard.set(sender.isOn, forKey: "FullImageCache")
    }
    
//    @IBOutlet weak var dataVersionLabel: UILabel!
    @IBOutlet weak var appVersionLabel: UILabel! {
        didSet {
            let infoDic = Bundle.main.infoDictionary
            appVersionLabel.text = (infoDic!["CFBundleShortVersionString"] as! String)
        }
    }
    
    func downloadAtStartValueChanged(_ sender: UISwitch) {
        UserDefaults.standard.set(sender.isOn, forKey: "DownloadAtStart")
    }
    
    @IBOutlet weak var sendEmailCell: UITableViewCell! {
        didSet {
            let tap = UITapGestureRecognizer.init(target: self, action: #selector(sendEmail))
            sendEmailCell.addGestureRecognizer(tap)
        }
    }
    func sendEmail() {
        // 首先要判断设备具不具备发送邮件功能
        if MFMailComposeViewController.canSendMail() {
            let controller = MFMailComposeViewController()
            controller.setSubject(NSLocalizedString("CGSSGuide问题反馈", comment: "设置页面"))
            controller.mailComposeDelegate = self
            controller.setToRecipients(["superk589@vip.qq.com"])
            if CGSSGlobal.languageType == .ja {
                controller.setCcRecipients(["gaiban@poketb.com"])
            }
            controller.addAttachmentData(DeviceInformationManager.default.toString().data(using: .utf8)!, mimeType: "text/plain", fileName: "device_information.txt")
            self.present(controller, animated: true, completion: nil)
        } else {
            let alert = UIAlertController.init(title: NSLocalizedString("打开邮箱失败", comment: "设置页面"), message: NSLocalizedString("未设置邮箱账户", comment: "设置页面"), preferredStyle: .alert)
            alert.addAction(UIAlertAction.init(title: NSLocalizedString("确定", comment: "设置页面"), style: .default, handler: nil))
            self.present(alert, animated: true, completion: nil)
        }
    }
    
    @IBOutlet weak var cacheImageCell: UITableViewCell! {
        didSet {
            let tap = UITapGestureRecognizer.init(target: self, action: #selector(cacheImage))
            cacheImageCell.addGestureRecognizer(tap)
        }
        
    }
    @IBOutlet weak var wipeDataCell: UITableViewCell! {
        didSet {
            let tap = UITapGestureRecognizer.init(target: self, action: #selector(wipeData))
            wipeDataCell.addGestureRecognizer(tap)
        }
    }
    
    func updateCacheSize() {
        DispatchQueue.global(qos: .userInitiated).async {
            if let cachePath = NSSearchPathForDirectoriesInDomains(FileManager.SearchPathDirectory.cachesDirectory, .userDomainMask, true).first {
                if let files = FileManager.default.subpaths(atPath: cachePath) {
                    var size = 0
                    for file in files {
                        let path = cachePath.appendingFormat("/\(file)")
                        if let attributes = try? FileManager.default.attributesOfItem(atPath: path) {
                            size += (attributes[FileAttributeKey.size] as! NSNumber).intValue
                        }
                    }
                    DispatchQueue.main.async(execute: { [weak self] in
                        self?.cacheSizeLabel.text = "\(size/(1024*1024))MB"
                    })
                }
            }
        }
    }
    @IBOutlet weak var cacheSizeLabel: UILabel! {
        didSet {
            cacheSizeLabel.text = "..MB"
        }
    }
    
    
    
    func wipeData() {
        if CGSSUpdater.default.isWorking {
            let alert = UIAlertController.init(title: NSLocalizedString("提示", comment: ""), message: NSLocalizedString("请等待更新完成或手动取消更新后，再尝试清除数据。", comment: ""), preferredStyle: .alert)
            alert.addAction(UIAlertAction.init(title: NSLocalizedString("确定", comment: ""), style: .cancel, handler: nil))
            self.present(alert, animated: true, completion: nil)
        } else {
            let wipeVC = WipeTableViewController()
            wipeVC.hidesBottomBarWhenPushed = true
            self.navigationController?.pushViewController(wipeVC, animated: true)
        }
    }
    
    func cacheImage() {
        if CGSSUpdater.default.isWorking {
            let alert = UIAlertController.init(title: NSLocalizedString("提示", comment: ""), message: NSLocalizedString("请等待更新完成或手动取消更新后，再尝试缓存图片。", comment: ""), preferredStyle: .alert)
            alert.addAction(UIAlertAction.init(title: NSLocalizedString("确定", comment: ""), style: .cancel, handler: nil))
            self.present(alert, animated: true, completion: nil)
        } else {
            let vc = DownloadImageController()
            vc.hidesBottomBarWhenPushed = true
            self.navigationController?.pushViewController(vc, animated: true)
        }
        
    }
    
    func cancelUpdate() {
        SDWebImagePrefetcher.shared().cancelPrefetching()
        CGSSUpdater.default.isUpdating = false
        let alvc = UIAlertController.init(title: NSLocalizedString("缓存图片取消", comment: "设置页面"), message: NSLocalizedString("缓存图片已被中止", comment: "设置页面"), preferredStyle: .alert)
        alvc.addAction(UIAlertAction.init(title: NSLocalizedString("确定", comment: "设置页面"), style: .cancel, handler: nil))
        self.tabBarController?.present(alvc, animated: true, completion: nil)
        //updateCacheSize()
    }
    
    func refresh() {
//        dataVersionLabel.text = CGSSVersionManager.default.currentDataVersionString
        updateCacheSize()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        refresh()
        NotificationCenter.default.addObserver(self, selector: #selector(refresh), name: .updateEnd, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(refresh), name: .saveEnd, object: nil)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        NotificationCenter.default.removeObserver(self)
    }
    
    @IBOutlet weak var reviewCell: UITableViewCell! {
        didSet {
            let tap = UITapGestureRecognizer.init(target: self, action: #selector(postReview))
            reviewCell.addGestureRecognizer(tap)
        }
    }
    func postReview() {
        // let url = "itms-apps://itunes.apple.com/WebObjects/MZStore.woa/wa/viewContentsUserReviews?type=Purple+Software&id=\(CGSSGlobal.appid)"
        guard let url = URL.init(string: "itms-apps://itunes.apple.com/app/id\(CGSSGlobal.appid)?action=write-review") else {
            return
        }
        UIApplication.shared.openURL(url)
    }
    
    var updateStatusView: UpdateStatusView!
    
    
    @IBOutlet weak var ackCell: UITableViewCell! {
        didSet {
            let tap = UITapGestureRecognizer.init(target: self, action: #selector(showAck))
            ackCell.addGestureRecognizer(tap)
        }
    }
    
    @IBOutlet weak var donateCell: UITableViewCell! {
        didSet {
            let tap = UITapGestureRecognizer.init(target: self, action: #selector(showDonate))
            donateCell.addGestureRecognizer(tap)
        }
    }
    @IBOutlet weak var licenseCell: UITableViewCell! {
        didSet {
            let tap = UITapGestureRecognizer.init(target: self, action: #selector(showLicense))
            licenseCell.addGestureRecognizer(tap)
        }
    }
    func showAck() {
        let ackVC = AcknowledgementViewController()
        ackVC.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(ackVC, animated: true)
    }
    
    func showLicense() {
        let licenseVC = LicenseViewController()
        licenseVC.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(licenseVC, animated: true)
    }
    
    func showDonate() {
        let donationVC = DonationViewController()
        donationVC.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(donationVC, animated: true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.tableFooterView = UIView.init(frame: CGRect.zero)
        updateStatusView = UpdateStatusView.init(frame: CGRect(x: 0, y: 0, width: 240, height: 50))
        updateStatusView.center = view.center
        updateStatusView.center.y = view.center.y - 120
        updateStatusView.isHidden = true
        updateStatusView.delegate = self
        UIApplication.shared.keyWindow?.addSubview(updateStatusView)
        
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false
        
        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}

//MARK: MFMailComposeViewControllerDelegate
extension SettingsTableViewController: MFMailComposeViewControllerDelegate, UINavigationControllerDelegate {
    // 发送邮件代理方法
    
    func mailComposeController(_ controller: MFMailComposeViewController, didFinishWith result: MFMailComposeResult, error: Error?) {
        controller.dismiss(animated: true, completion: nil)
//        switch result{
//        case MFMailComposeResultSent:
//            let alert = UIAlertController.init(title: "邮件发送成功", message: "", preferredStyle: .Alert)
//            alert.addAction(UIAlertAction.init(title: "确定", style: .Default, handler: nil))
//            self.presentViewController(alert, animated: true, completion: nil)
//        case MFMailComposeResultCancelled:
//            break //print("邮件已取消")
//        case MFMailComposeResultSaved:
//            break //print("邮件已保存")
//        case MFMailComposeResultFailed:
//            let alert = UIAlertController.init(title: "邮件发送失败", message: "", preferredStyle: .Alert)
//            alert.addAction(UIAlertAction.init(title: "确定", style: .Default, handler: nil))
//            self.presentViewController(alert, animated: true, completion: nil)
//        default:
//            //print("邮件没有发送")
//            break
//        }
    }
}
