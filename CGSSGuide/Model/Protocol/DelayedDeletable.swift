//
//  DelayedDeletable.swift
//  CGSSGuide
//
//  Created by Daniel Eggert on 23/08/2015.
//  Copyright © 2015 objc.io. All rights reserved.
//

import CoreData

private let MarkedForDeletionDateKey = "markedForDeletionDate"

public protocol DelayedDeletable: class {
    var changedForDelayedDeletion: Bool { get }
    var markedForDeletionDate: Date? { get set }
    func markForLocalDeletion()
}


extension DelayedDeletable {
    public static var notMarkedForLocalDeletionPredicate: NSPredicate {
        return NSPredicate(format: "%K == NULL", MarkedForDeletionDateKey)
    }
}


extension DelayedDeletable where Self: NSManagedObject {
    public var changedForDelayedDeletion: Bool {
        return changedValue(forKey: MarkedForDeletionDateKey) as? Date != nil
    }

    /// Mark an object to be deleted at a later point in time.
    /// An object marked for local deletion will no longer match the
    /// `notMarkedForDeletionPredicate`.
    public func markForLocalDeletion() {
        guard isFault || markedForDeletionDate == nil else { return }
        markedForDeletionDate = Date()
    }
    
}


/// Objects that have been marked for local deletion more than this time (in seconds) ago will get permanently deleted.
private let DeletionAgeBeforePermanentlyDeletingObjects = TimeInterval(2 * 60)

extension NSManagedObjectContext {
    
    public func batchDeleteObjectsMarkedForLocalDeletion() {
        Unit.batchDeleteObjectsMarkedForLocalDeletionInContext(self)
        FavoriteCard.batchDeleteObjectsMarkedForLocalDeletionInContext(self)
        FavoriteChara.batchDeleteObjectsMarkedForLocalDeletionInContext(self)
    }
    
}


extension DelayedDeletable where Self: NSManagedObject, Self: Managed {
    
    fileprivate static func batchDeleteObjectsMarkedForLocalDeletionInContext(_ managedObjectContext: NSManagedObjectContext) {
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: self.entityName)
        let cutoff = Date(timeIntervalSinceNow: -DeletionAgeBeforePermanentlyDeletingObjects)
        fetchRequest.predicate = NSPredicate(format: "%K < %@", MarkedForDeletionDateKey, cutoff as NSDate)
        let batchRequest = NSBatchDeleteRequest(fetchRequest: fetchRequest)
        batchRequest.resultType = .resultTypeStatusOnly
        try! managedObjectContext.execute(batchRequest)
    }

//    fileprivate static func batchDeleteObjectsMarkedForLocalDeletionInContext(_ managedObjectContext: NSManagedObjectContext) {
//        let fetchRequest = NSFetchRequest<NSManagedObject>(entityName: self.entityName)
//        let cutoff = Date(timeIntervalSinceNow: -DeletionAgeBeforePermanentlyDeletingObjects)
//        fetchRequest.predicate = NSPredicate(format: "%K < %@", MarkedForDeletionDateKey, cutoff as NSDate)
//        let objects = try! managedObjectContext.fetch(fetchRequest)
//
//        objects.forEach { managedObjectContext.delete($0) }
//
//        managedObjectContext.saveOrRollback()
//    }
}


