package tw.gov.twc.utils;

import javax.naming.RefAddr;
import javax.naming.Reference;

import org.apache.log4j.Logger;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

public class EncDataSourceFactory extends BasicDataSourceFactory {
	
	private Logger log = Logger.getLogger(EncDataSourceFactory.class);
	
	@Override
	@SuppressWarnings("rawtypes")
	public Object getObjectInstance(Object obj, javax.naming.Name name, javax.naming.Context nameCtx,
			java.util.Hashtable environment) throws Exception {

		if ((obj == null) || !(obj instanceof Reference)) {
			return null;
		}

		Reference ref = (Reference) obj;

		RefAddr ra = null;
		int len = ref.size();
		for (int i = 0; i < len; i++) {
			ra = ref.get(i);
			log.info("type" + (i+1) + ":" + ra.getType());
			if ("password".equalsIgnoreCase(ra.getType())) {
				ref.remove(i);
				ref.add(i, new TransformRefAddr(ra) {
					private static final long serialVersionUID = 1L;
					@Override
					public Object transform(Object obj) {
						return AesUtility.decrypt(obj.toString());
					}
				});
			}
		}

		return super.getObjectInstance(obj, name, nameCtx, environment);
	};

	private abstract class TransformRefAddr extends RefAddr {
		private static final long serialVersionUID = 1L;

		private RefAddr refAddr;

		public TransformRefAddr(RefAddr refAddr) {
			super(refAddr.getType());
			this.refAddr = refAddr;
		}
		@Override
		public Object getContent() {

			return this.transform(refAddr.getContent());
		}

		public abstract Object transform(Object obj);

	}
}